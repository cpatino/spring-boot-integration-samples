# AWS Local Environment Setup
The docker directory contains the necessary files to run a container with localstack that will be used for testing the application locally. The localstack container will simulate AWS services, allowing you to test your application without needing to connect to actual AWS resources.

How to start local environment:

1. Before start docker container (only needed one time), run the following command to make the script executable:
<code>chmod +x init-aws.sh</code> this will allow you to run the script that initializes the AWS services in localstack.
2. Run the command <code>docker-compose up -d</code> to start the localstack container in detached mode. This will set up the local AWS environment for you.
3. Start the application. The application will connect to the localstack container to access the simulated AWS services.

# How to publish messages to AWS SQS Service [Localstack]

1. Configure the dependency needed to connect to SQS in the pom.xml file. This allows the application to use the AWS SDK for Java to interact with SQS.

        <dependency>
            <groupId>io.awspring.cloud</groupId>
            <artifactId>spring-cloud-aws-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>io.awspring.cloud</groupId>
            <artifactId>spring-cloud-aws-starter-sqs</artifactId>
        </dependency>
2. Configure the AWS endpoint, credentials and region in the application.properties file.

         spring.cloud.aws.credentials.access-key=000000000000
         spring.cloud.aws.credentials.secret-key=000000000000
         spring.cloud.aws.region.static=eu-west-1
         spring.cloud.aws.sqs.endpoint=http://localhost:4566
    The 'aws.sqs.endpoint' property points to the 'localstack' container, while the aws.accessKeyId and aws.secretAccessKey properties are set to dummy values since 'localstack' does not require valid AWS credentials.

3. Inject the ```SqsTemplate``` (Autoconfigured by spring-cloud-aws-starter-sqs) to send messages to the SQS queue. This allows the application to publish messages to the SQS queue that is simulated by 'localstack'.

        sqsTemplate.send(to -> to.queue(QUEUE_NAME).payload(content));
    
With this setup, you can test your application locally using 'localstack' to simulate AWS services, allowing you to develop and debug your application without needing to connect to actual AWS resources, and also being ready to deploy to AWS.

Once you have finished testing your application locally, you are ready to deploy to consume the SQS service in AWS. You can simply change the AWS endpoint, credentials and region in the application.properties file (in a secure way) to point to the actual AWS services, and your application will be able to interact with the real SQS service in AWS.

# How to publish messages to AWS SNS Service [Localstack]

1. Configure the dependency needed to connect to SNS in the pom.xml file. This allows the application to use the AWS SDK for Java to interact with SQS.

        <dependency>
            <groupId>io.awspring.cloud</groupId>
            <artifactId>spring-cloud-aws-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>io.awspring.cloud</groupId>
            <artifactId>spring-cloud-aws-starter-sns</artifactId>
        </dependency>
2. Configure the AWS SNS endpoint, credentials and region in the application.properties file.

         spring.cloud.aws.credentials.access-key=000000000000
         spring.cloud.aws.credentials.secret-key=000000000000
         spring.cloud.aws.region.static=eu-west-1
         spring.cloud.aws.sns.endpoint=http://localhost:4566
   The 'aws.sns.endpoint' property points to the 'localstack' container, while the aws.accessKeyId and aws.secretAccessKey properties are set to dummy values since 'localstack' does not require valid AWS credentials.

3. Inject the ```SnsTemplate``` (Autoconfigured by spring-cloud-aws-starter-sns) to send notifications to the SNS topic. This allows the application to publish events to the SNS topic that is simulated by 'localstack'.

        snsTemplate.sendNotification(TOPIC_NAME, content, null);

With this setup, you can test your application locally using 'localstack' to simulate AWS services, allowing you to develop and debug your application without needing to connect to actual AWS resources, and also being ready to deploy to AWS.

Once you have finished testing your application locally, you are ready to deploy to consume the SNS service in AWS. You can simply change the AWS endpoint, credentials and region in the application.properties file (in a secure way) to point to the actual AWS services, and your application will be able to interact with the real SNS service in AWS.
