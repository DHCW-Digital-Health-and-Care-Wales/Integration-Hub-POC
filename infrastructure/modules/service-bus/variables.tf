variable "resource_group_name" {
  description = "The name of the resource group"
  type        = string
}

variable "location" {
  description = "The Azure region where resources will be created"
  type        = string
}

variable "service_bus_namespace" {
  description = "Name of the Service Bus namespace"
  type        = string
}

variable "service_bus_queue_name" {
  description = "Name of the Service Bus queue"
  type        = string
}

variable "service_bus_topic_name" {
  description = "Name of the Service Bus topic"
  type        = string
}

variable "service_bus_subscription_name" {
  description = "Name of the Service Bus subscription"
  type        = string
}

variable "environment" {
  description = "Environment (dev, test, prod)"
  type        = string
}
