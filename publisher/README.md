# AWS Local Environment Setup
The docker directory contains the necessary files to run a container with localstack that will be used for testing the application locally. The localstack container will simulate AWS services, allowing you to test your application without needing to connect to actual AWS resources.

How to start local environment:

1. Before start docker container (only needed one time), run the following command to make the script executable:
<code>chmod +x init-aws.sh</code> this will allow you to run the script that initializes the AWS services in localstack.
2. Run the command <code>docker-compose up -d</code> to start the localstack container in detached mode. This will set up the local AWS environment for you.
3. Start the application. The application will connect to the localstack container to access the simulated AWS services.

# How this application publish messages to AWS SQS Service

1. Configure the dependency needed to connect to SQS in the pom.xml file. This allows the application to use the AWS SDK for Java to interact with SQS.

        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>sqs</artifactId>
        </dependency>
2. Configure the AWS endpoint, credentials and region in the application.properties file.

         aws.endpoint=http://localhost:4566
         aws.region=eu-west-1
         aws.accessKeyId=000000000000
         aws.secretAccessKey=000000000000
    The 'aws.endpoint' property points to the 'localstack' container, while the aws.accessKeyId and aws.secretAccessKey properties are set to dummy values since 'localstack' does not require valid AWS credentials.

3. Create a Bean for the SQS client. This allows the application to create an instance of the SQS client that can be used to interact with the SQS service.

        @Bean
        public SqsClient sqsClient() {
            return SqsClient.builder()
                    .endpointOverride(URI.create(awsEndpoint))
                    .region(Region.of(awsRegion))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey)))
                    .build();
        }
4. Use the SQS client to send messages to the SQS queue. This allows the application to publish messages to the SQS queue that is simulated by 'localstack'.

        public void sendMessage(String message) {
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .build();
            sqsClient.sendMessage(sendMsgRequest);
        }
    Note: Make sure to replace 'queueUrl' with the actual URL of the SQS queue that you want to send messages to. You can obtain the queue URL from the 'localstack' container after it has been initialized or programmatically get it using the SQS client 

         sqsClient.getQueueUrl(builder -> builder.queueName(QUEUE_NAME)).queueUrl()
With this setup, you can test your application locally using 'localstack' to simulate AWS services, allowing you to develop and debug your application without needing to connect to actual AWS resources, and also being ready to deploy to AWS.

Once you have finished testing your application locally, you are ready to deploy to consume the SQS service in AWS. You can simply change the AWS endpoint, credentials and region in the application.properties file (in a secure way) to point to the actual AWS services, and your application will be able to interact with the real SQS service in AWS.