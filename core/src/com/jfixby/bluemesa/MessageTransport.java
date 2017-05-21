
package com.jfixby.bluemesa;

import java.util.HashMap;

import com.amazonaws.util.Base64;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.aws.api.AWS;
import com.jfixby.scarabei.aws.api.AWSCredentialsProvider;
import com.jfixby.scarabei.aws.api.sqs.SQS;
import com.jfixby.scarabei.aws.api.sqs.SQSClienSpecs;
import com.jfixby.scarabei.aws.api.sqs.SQSClient;
import com.jfixby.scarabei.aws.api.sqs.SQSCreateQueueParams;
import com.jfixby.scarabei.aws.api.sqs.SQSCreateQueueResult;
import com.jfixby.scarabei.aws.api.sqs.SQSSendMessageParams;

public class MessageTransport {

	private final AWSCredentialsProvider awsKeys = new BlueMesaAWSCredentials();
	private final SQSClient client;
	private final String queuURL;

	public MessageTransport (final MessageTransportSpecs t_specs) {

		final SQS sqs = AWS.getSQS();
		final SQSClienSpecs sqsspecs = sqs.newSQSClienSpecs();
		sqsspecs.setAWSCredentialsProvider(this.awsKeys);

		this.client = sqs.newClient(sqsspecs);

		final SQSCreateQueueParams createQueueRequestParams = sqs.newCreateQueueParams();
		createQueueRequestParams.setName("BlueMesa-gas-" + t_specs.deviceID);

		final SQSCreateQueueResult queueCreateResult = this.client.createQueue(createQueueRequestParams);
		this.queuURL = queueCreateResult.getQueueURL();
		L.d("open queue", this.queuURL);

		final Collection<String> currentQueues = this.client.listAllSQSUrls();
		currentQueues.print("current queues");

	}

	public void send (final GasSensorMessage message) {
		final SQS sqs = AWS.getSQS();
		final SQSSendMessageParams sendParams = sqs.newSendMessageParams();
		sendParams.setQueueURL(this.queuURL);

		final HashMap<String, String> msg = new HashMap<String, String>();
		msg.put("global-counter", message.globalCounter + "");
		msg.put("data-block-64-Base64", Base64.encodeAsString(message.data));
		final String messageText = Json.serializeToString(msg).toString();
		L.d(messageText);
		sendParams.setBody(messageText);
		this.client.sendMessage(sendParams);
	}

}
