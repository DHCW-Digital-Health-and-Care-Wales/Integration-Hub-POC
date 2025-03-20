# This file should be created after the storage account is provisioned
# Initially comment out this block, run terraform init and apply to create storage,
# then uncomment and run terraform init again

/*
terraform {
  backend "azurerm" {
    resource_group_name  = "terraform-state-rg"
    storage_account_name = "tfstate<unique_suffix>"
    container_name       = "tfstate"
    key                  = "terraform.tfstate"
  }
}
*/
