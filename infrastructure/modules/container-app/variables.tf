variable "resource_group_name" {
  description = "The name of the resource group"
  type        = string
}

variable "location" {
  description = "The Azure region where resources will be created"
  type        = string
}

variable "container_app_name" {
  description = "Name of the Azure Container App"
  type        = string
}

variable "container_app_environment_name" {
  description = "Name of the Container App Environment"
  type        = string
}

variable "container_registry_login_server" {
  description = "Login server for the container registry"
  type        = string
}

variable "container_registry_name" {
  description = "Name of the container registry"
  type        = string
}

variable "container_registry_id" {
  description = "ID of the container registry"
  type        = string
}

variable "container_app_image" {
  description = "Container image to deploy (without registry name)"
  type        = string
}

variable "service_bus_namespace_name" {
  description = "Name of the Service Bus namespace"
  type        = string
}

variable "service_bus_namespace_id" {
  description = "ID of the Service Bus namespace"
  type        = string
}

variable "environment" {
  description = "Environment (dev, test, prod)"
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
