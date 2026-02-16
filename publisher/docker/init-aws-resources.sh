#!/bin/bash
set -euo pipefail

REGION="eu-west-1"
TOPIC_NAME="my-topic"
QUEUE_NAME="my-queue"

# Create SNS topic and obtain its ARN
TOPIC_ARN=$(awslocal sns create-topic --name "$TOPIC_NAME" --region "$REGION" --query 'TopicArn' --output text)

# Create SQS queue and obtain its URL
QUEUE_URL=$(awslocal sqs create-queue --queue-name "$QUEUE_NAME" --region "$REGION" --query 'QueueUrl' --output text)

# GET the SQS queue ARN
QUEUE_ARN=$(awslocal sqs get-queue-attributes --queue-url "$QUEUE_URL" --attribute-names QueueArn --region "$REGION" --query 'Attributes.QueueArn' --output text)

# Subscribe the SQS queue to the SNS topic
awslocal sns subscribe --topic-arn "$TOPIC_ARN" --protocol sqs --notification-endpoint "$QUEUE_ARN" --region "$REGION"