
package com.jfixby.bluemesa.sqs;

import com.jfixby.bluemesa.GasSensorMessage;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.aws.api.AWS;
import com.jfixby.scarabei.aws.api.AWSCredentialsProvider;
import com.jfixby.scarabei.aws.api.sqs.SQS;
import com.jfixby.scarabei.aws.api.sqs.SQSClienSpecs;
import com.jfixby.scarabei.aws.api.sqs.SQSClient;
import com.jfixby.scarabei.aws.api.sqs.SQSCreateQueueParams;
import com.jfixby.scarabei.aws.api.sqs.SQSCreateQueueResult;
import com.jfixby.scarabei.aws.api.sqs.SQSDeleteMessageParams;
import com.jfixby.scarabei.aws.api.sqs.SQSDeleteMessageResult;
import com.jfixby.scarabei.aws.api.sqs.SQSMessage;
import com.jfixby.scarabei.aws.api.sqs.SQSReceiveMessageParams;
import com.jfixby.scarabei.aws.api.sqs.SQSReceiveMessageRequest;
import com.jfixby.scarabei.aws.api.sqs.SQSReceiveMessageResult;
import com.jfixby.scarabei.aws.api.sqs.SQSSendMessageParams;

public class DesktopMessageTransport implements MessageTransport {

	private final AWSCredentialsProvider awsKeys = this.newBlueMesaAWSCredentials();
	private final SQSClient client;
	private final String queuURL;
	private final String deviceID;

	public DesktopMessageTransport (final MessageTransportSpecs t_specs) {

		final SQS sqs = AWS.getSQS();
		final SQSClienSpecs sqsspecs = sqs.newSQSClienSpecs();
		sqsspecs.setAWSCredentialsProvider(this.awsKeys);

		this.client = sqs.newClient(sqsspecs);

		final SQSCreateQueueParams createQueueRequestParams = sqs.newCreateQueueParams();
		createQueueRequestParams.setName("BlueMesa-gas-" + t_specs.deviceID);
		this.deviceID = t_specs.deviceID;

		final SQSCreateQueueResult queueCreateResult = this.client.createQueue(createQueueRequestParams);
		this.queuURL = queueCreateResult.getQueueURL();
		L.d("open queue", this.queuURL);

		final Collection<String> currentQueues = this.client.listAllSQSUrls();
		currentQueues.print("current queues");

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

	@Override
	public void send (final GasSensorMessage message) {
		final SQS sqs = AWS.getSQS();
		final SQSSendMessageParams sendParams = sqs.newSendMessageParams();
		sendParams.setQueueURL(this.queuURL);

		final String messageText = Json.serializeToString(message).toString();
// L.d(messageText);
		sendParams.setBody(messageText);
		this.client.sendMessage(sendParams);
	}

	public List<GasSensorMessage> receive () {
		final SQS sqs = AWS.getSQS();
		final SQSReceiveMessageParams params = sqs.newReceiveMessageParams();
		params.setQueueURL(this.queuURL);
		final SQSReceiveMessageRequest request = sqs.newReceiveMessageRequest(params);

		final SQSReceiveMessageResult result = this.client.receive(request);

		final Collection<SQSMessage> messages = result.listMessages();
		final List<GasSensorMessage> list = Collections.newList();
		for (final SQSMessage m : messages) {
// m.print();
			list.add(this.process(m));

		}
		return list;
	}

	private GasSensorMessage process (final SQSMessage inputMessage) {
		final String inputMessageBody = inputMessage.getBody();
		final String inputMessageReceiptHandle = inputMessage.getReceiptHandle();

		final GasSensorMessage srlzd_notification = this.readMessage(inputMessageBody);

		final SQS sqs = AWS.getSQS();
		final SQSDeleteMessageParams delete = sqs.newDeleteMessageParams();
		delete.setQueueURL(this.queuURL);
		delete.setMessageReceiptHandle(inputMessageReceiptHandle);
		final SQSDeleteMessageResult deleteResult = this.client.deleteMessage(delete);

		return srlzd_notification;

	}

	private GasSensorMessage readMessage (final String inputMessageBody) {
		return Json.deserializeFromString(GasSensorMessage.class, inputMessageBody);
	}

}
