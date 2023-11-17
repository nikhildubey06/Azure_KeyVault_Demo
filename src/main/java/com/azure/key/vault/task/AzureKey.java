package com.azure.key.vault.task;

import com.azure.core.util.polling.SyncPoller;
import com.azure.identity.DefaultAzureCredentialBuilder;

import com.azure.security.keyvault.keys.KeyClient;
import com.azure.security.keyvault.keys.KeyClientBuilder;
import com.azure.security.keyvault.keys.models.DeletedKey;
import com.azure.security.keyvault.keys.models.KeyType;
import com.azure.security.keyvault.keys.models.KeyVaultKey;

public class AzureKey {
    public static void main(String[] args) throws InterruptedException, IllegalArgumentException {
        String keyVaultName = System.getenv("KEY_VAULT_NAME");
        String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";

        System.out.printf("key vault name = %s and key vault URI = %s \n", keyVaultName, keyVaultUri);

        KeyClient keyClient = new KeyClientBuilder()
             .vaultUrl(keyVaultUri)
             .credential(new DefaultAzureCredentialBuilder().build())
             .buildClient();

        String keyName = "myKey";

        System.out.print("Creating a key in " + keyVaultName + " called '" + keyName + " ... ");

        // create key
        keyClient.createKey(keyName, KeyType.RSA);

        System.out.print("done.");
        System.out.println("Retrieving key from " + keyVaultName + ".");

        // retrieve key
        KeyVaultKey retrievedKey = keyClient.getKey(keyName);

        System.out.println("Your key's ID is '" + retrievedKey.getId() + "'.");
        System.out.println("Deleting your key from " + keyVaultName + " ... ");

       // delete key
        SyncPoller<DeletedKey, Void> deletionPoller = keyClient.beginDeleteKey(keyName); 
        deletionPoller.waitForCompletion();

        System.out.print("done.");
    }
}
