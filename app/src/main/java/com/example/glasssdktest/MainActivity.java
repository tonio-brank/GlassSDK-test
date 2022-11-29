package com.example.glasssdktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mypos.glasssdk.Currency;
import com.mypos.glasssdk.MyPOSAPI;
import com.mypos.glasssdk.MyPOSPayment;
import com.mypos.glasssdk.OnPOSInfoListener;
import com.mypos.glasssdk.ReferenceType;
import com.mypos.glasssdk.TransactionProcessingResult;
import com.mypos.glasssdk.data.POSInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button payBtn;
    private Currency currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        payBtn = findViewById(R.id.buttonPay);
        payBtn.setOnClickListener(this);

        MyPOSAPI.registerPOSInfo(MainActivity.this, new OnPOSInfoListener() {
            @Override
            public void onReceive(POSInfo info) {
//                Toast.makeText(MainActivity.this, "on a recu qqch", Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, info.toString(), Toast.LENGTH_SHORT).show();
                Log.i("info", info.toString());
                currency = Currency.valueOf(info.getCurrencyName());
            }

        });

    }

    void pay(){
        // Build the payment call
        MyPOSPayment payment = MyPOSPayment.builder()
                // Mandatory parameters
                .productAmount(1.)
                .currency(currency)
                // Foreign transaction ID. Maximum length: 128 characters
//                .foreignTransactionId(UUID.randomUUID().toString())
                // Optional parameters
                // Enable tipping mode
//                .tippingModeEnabled(true)
//                .tipAmount(1.55)
//                // Operator code. Maximum length: 4 characters
//                .operatorCode("1234")
//                // Reference number. Maximum length: 20 alpha numeric characters
//                .reference("asd123asd", ReferenceType.REFERENCE_NUMBER)
//                // card scheme brandnig
//                .mastercardSonicBranding(true)
//                .visaSensoryBranding(true)
//                // Set receipt mode if printer is paired
//                .printMerchantReceipt(MyPOSUtil.RECEIPT_ON) // possible options RECEIPT_ON, RECEIPT_OFF
//                .printCustomerReceipt(MyPOSUtil.RECEIPT_ON) // possible options RECEIPT_ON, RECEIPT_OFF, RECEIPT_AFTER_CONFIRMATION, RECEIPT_E_RECEIPT
//                //set email or phone e-receipt receiver, works with customer receipt configuration RECEIPT_E_RECEIPT or RECEIPT_AFTER_CONFIRMATION
//                .eReceiptReceiver("examplename@example.com")
                .build();

        // Start the transaction
        MyPOSAPI.openPaymentActivity(MainActivity.this, payment, 1);
    }

//    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The same request code as when calling MyPOSAPI.openPaymentActivity
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Log.e("DATA", key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
            }
        }
        if (requestCode == 1) {
            // The transaction was processed, handle the response
            if (resultCode == RESULT_OK) {
                // Something went wrong in the Payment core app and the result couldn't be returned properly
                if (data == null) {
                    Toast.makeText(this, "Transaction cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }
                int transactionResult = data.getIntExtra("status", TransactionProcessingResult.TRANSACTION_FAILED);

                Toast.makeText(this, "Payment transaction has completed. Result: " + transactionResult, Toast.LENGTH_SHORT).show();

                // TODO: handle each transaction response accordingly
                if (transactionResult == TransactionProcessingResult.TRANSACTION_SUCCESS) {
                    // Transaction is successful
                    Toast.makeText(this, "Payment Successful" + transactionResult, Toast.LENGTH_SHORT).show();
                }
            } else {
                // The user cancelled the transaction
                Toast.makeText(this, "Transaction cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonPay:
//
                pay();
                break;
        }
    }
}