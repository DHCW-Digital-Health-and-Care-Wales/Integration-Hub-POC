variable "location" {
  description = "The Azure region where resources will be created"
  type        = string
  default     = "uksouth"
}

variable "resource_group_name" {
  description = "The name of the resource group"
  type        = string
  default     = "my-app-rg"
}

variable "environment" {
  description = "Environment (dev, test, prod)"
  type        = string
  default     = "dev"
}

variable "storage_account_name" {
  description = "Name of the storage account for Terraform state"
  type        = string
  default     = "tfstate"
}

variable "service_bus_namespace" {
  description = "Name of the Service Bus namespace"
  type        = string
  default     = "myapp-servicebus"
}

variable "service_bus_queue_name" {
  description = "Name of the Service Bus queue"
  type        = string
  default     = "myapp-queue"
}

variable "service_bus_topic_name" {
  description = "Name of the Service Bus topic"
  type        = string
  default     = "myapp-topic"
}

variable "service_bus_subscription_name" {
  description = "Name of the Service Bus subscription"
  type        = string
  default     = "myapp-subscription"
}

variable "container_registry_name" {
  description = "Name of the Azure Container Registry"
  type        = string
  default     = "myappregistry"
}

variable "container_app_name" {
  description = "Name of the Azure Container App"
  type        = string
  default     = "myapp"
}

variable "container_app_image" {
  description = "Container image to deploy (without registry name)"
  type        = string
  default     = "myapp:latest"
}

variable "container_app_environment_name" {
  description = "Name of the Container App Environment"
  type        = string
  default     = "myapp-env"
}
