
package com.jfixby.bluemesa.sqs;

import com.jfixby.bluemesa.GasSensorMessage;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.aws.api.AWSCredentialsProvider;
import com.jfixby.scarabei.aws.api.sqs.SQS;
import com.jfixby.scarabei.aws.api.sqs.SQSClienSpecs;
import com.jfixby.scarabei.aws.api.sqs.SQSClient;
import com.jfixby.scarabei.aws.api.sqs.SQSComponent;
import com.jfixby.scarabei.aws.api.sqs.SQSCreateQueueParams;
import com.jfixby.scarabei.aws.api.sqs.SQSCreateQueueResult;
import com.jfixby.scarabei.aws.api.sqs.SQSDeleteMessageParams;
import com.jfixby.scarabei.aws.api.sqs.SQSDeleteMessageResult;
import com.jfixby.scarabei.aws.api.sqs.SQSMessage;
import com.jfixby.scarabei.aws.api.sqs.SQSReceiveMessageParams;
import com.jfixby.scarabei.aws.api.sqs.SQSReceiveMessageRequest;
import com.jfixby.scarabei.aws.api.sqs.SQSReceiveMessageResult;
import com.jfixby.scarabei.aws.api.sqs.SQSSendMessageParams;

public class BlureMesaMessageTransport implements MessageTransport {

	private final AWSCredentialsProvider awsKeys = this.newBlueMesaAWSCredentials();
	private final SQSClient client;
	private final MessagesConsumer ep;

	public BlureMesaMessageTransport (final MessageTransportSpecs t_specs, final MessagesConsumer ep) {

		final SQSComponent sqs = SQS.invoke();
		final SQSClienSpecs sqsspecs = sqs.newSQSClienSpecs();
		sqsspecs.setAWSCredentialsProvider(this.awsKeys);

		this.client = sqs.newClient(sqsspecs);

// L.d("open queue", queuURL);

		final Collection<String> currentQueues = this.client.listAllSQSUrls();
		currentQueues.print("current queues");
		this.ep = ep;

	}

	private AWSCredentialsProvider newBlueMesaAWSCredentials () {
		final ClassLoader classLoader = this.getClass().getClassLoader();
		final String className = "com.jfixby.bluemesa.BlueMesaAWSCredentials";
		try {
			final Class<AWSCredentialsProvider> klass = (Class<AWSCredentialsProvider>)Class.forName(className, true, classLoader);
			return klass.newInstance();
		} catch (final Throwable e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String removeNonDigits (final String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		return str.replaceAll("[^0-9]+", "");
	}

	@Override
	public void send (final GasSensorMessage message) {
		final SQSComponent sqs = SQS.invoke();

		final SQSCreateQueueParams createQueueRequestParams = sqs.newCreateQueueParams();

		final String queueName = "BlueMesa-gas-" + (message.deviceID).replaceAll(":", "");

		createQueueRequestParams.setName(queueName);

		L.d("create queue", queueName);
		final SQSCreateQueueResult queueCreateResult = this.client.createQueue(createQueueRequestParams);
		final String queuURL = queueCreateResult.getQueueURL();

		final SQSSendMessageParams sendParams = sqs.newSendMessageParams();
		sendParams.setQueueURL(queuURL);

		final String messageText = Json.serializeToString(message).toString();
		sendParams.setBody(messageText);
		message.print();
		this.client.sendMessage(sendParams);
	}

	public List<GasSensorMessage> receive (final String deviceID) {
		final SQSComponent sqs = SQS.invoke();
		final SQSReceiveMessageParams params = sqs.newReceiveMessageParams();

		final SQSCreateQueueParams createQueueRequestParams = sqs.newCreateQueueParams();
		createQueueRequestParams.setName("BlueMesa-gas-" + deviceID);

		final SQSCreateQueueResult queueCreateResult = this.client.createQueue(createQueueRequestParams);
		final String queuURL = queueCreateResult.getQueueURL();

		params.setQueueURL(queuURL);
		final SQSReceiveMessageRequest request = sqs.newReceiveMessageRequest(params);

		final SQSReceiveMessageResult result = this.client.receive(request);

		final Collection<SQSMessage> messages = result.listMessages();
		final List<GasSensorMessage> list = Collections.newList();
		for (final SQSMessage m : messages) {
// m.print();
			list.add(this.process(m, queuURL));

		}
		return list;
	}

	private GasSensorMessage process (final SQSMessage inputMessage, final String queuURL) {
		final String inputMessageBody = inputMessage.getBody();
		final String inputMessageReceiptHandle = inputMessage.getReceiptHandle();

		final GasSensorMessage srlzd_notification = this.readMessage(inputMessageBody);

		final SQSComponent sqs = SQS.invoke();
		final SQSDeleteMessageParams delete = sqs.newDeleteMessageParams();
		delete.setQueueURL(queuURL);
		delete.setMessageReceiptHandle(inputMessageReceiptHandle);
		final SQSDeleteMessageResult deleteResult = this.client.deleteMessage(delete);

		return srlzd_notification;

	}

	private GasSensorMessage readMessage (final String inputMessageBody) {
		return Json.deserializeFromString(GasSensorMessage.class, inputMessageBody);
	}

}
