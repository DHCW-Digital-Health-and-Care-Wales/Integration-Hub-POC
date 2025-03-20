resource "azurerm_container_registry" "main" {
  name                = var.container_registry_name
  resource_group_name = var.resource_group_name
  location            = var.location
  sku                 = "Standard"
  admin_enabled       = true

  tags = {
    Environment = var.environment
  }
}
