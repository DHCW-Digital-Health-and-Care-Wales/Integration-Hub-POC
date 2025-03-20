resource "azurerm_resource_group" "main" {
  name     = var.resource_group_name
  location = var.location
  tags = {
    Environment = var.environment
  }
}

# State Storage - Create this first and then configure backend
module "storage" {
  source              = "./modules/storage"
  resource_group_name = azurerm_resource_group.main.name
  location            = var.location
  storage_account_name = "${var.storage_account_name}${random_string.suffix.result}"
  environment         = var.environment
}

# Service Bus
module "service_bus" {
  source                      = "./modules/service-bus"
  resource_group_name         = azurerm_resource_group.main.name
  location                    = var.location
  service_bus_namespace       = "${var.service_bus_namespace}-${var.environment}"
  service_bus_queue_name      = var.service_bus_queue_name
  service_bus_topic_name      = var.service_bus_topic_name
  service_bus_subscription_name = var.service_bus_subscription_name
  environment                 = var.environment
}

# Container Registry
module "container_registry" {
  source                 = "./modules/container-registry"
  resource_group_name    = azurerm_resource_group.main.name
  location               = var.location
  container_registry_name = "${var.container_registry_name}${random_string.suffix.result}"
  environment            = var.environment
}

# Container App
module "container_app" {
  source                        = "./modules/container-app"
  resource_group_name           = azurerm_resource_group.main.name
  location                      = var.location
  container_app_name            = var.container_app_name
  container_app_environment_name = var.container_app_environment_name
  container_registry_login_server = module.container_registry.login_server
  container_registry_name        = module.container_registry.name
  container_app_image           = var.container_app_image
  service_bus_namespace_name    = module.service_bus.namespace_name
  service_bus_namespace_id      = module.service_bus.namespace_id
  container_registry_id         = module.container_registry.id
  service_bus_queue_name        = module.service_bus.queue_name
  service_bus_topic_name        = module.service_bus.topic_name
  service_bus_subscription_name = module.service_bus.subscription_name
  environment                   = var.environment

  depends_on = [
    module.container_registry,
    module.service_bus
  ]
}

# Random string for unique names
resource "random_string" "suffix" {
  length  = 6
  special = false
  upper   = false
}
