package com.example.animaldispersal.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.animaldispersal.AppController;
import com.example.davaodemo.R;

import org.ndeftools.Message;
import org.ndeftools.MimeRecord;
import org.ndeftools.Record;
import org.ndeftools.externaltype.ExternalTypeRecord;
import org.ndeftools.wellknown.TextRecord;

import java.io.IOException;

/**
 * Created by user on 20/12/2016.
 */
public abstract class NfcWriteActivity extends NfcReadActivity {

    private boolean readOnly;
    private String readAnimalId ="";
    private String newAnimalId;
    protected Message message;

    private static final String TAG = NfcWriteActivity.class.getName();


    public void setNfcWriteValues(boolean readOnly, String animalId){
        this.readOnly = readOnly;
        this.newAnimalId = animalId;
    }

    public void nfcIntentDetected(Intent intent, String action) {
        // then write
        if (readOnly)
            read(intent,action);
        else
            read(intent,action);
            write(createNdefMessage(), intent);
    }

    // NFC Write Methods
    public boolean write(Message message, Intent intent) {
        //compare read animal Id with animal Id to write
        Log.d(TAG, "readAnimalId: "+readAnimalId);
        Log.d(TAG, "newAnimalId: "+newAnimalId);
        if (readAnimalId == newAnimalId) {
            return write(message.getNdefMessage(), intent);
        }
        else {
            return nfcFailAnimalIdCheck();
        }
    }

    public boolean write(NdefMessage rawMessage, Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d(TAG, "readAnimalId: "+readAnimalId);
        Log.d(TAG, "newAnimalId: "+newAnimalId);
        if (!newAnimalId.equalsIgnoreCase(readAnimalId))
            return nfcFailAnimalIdCheck();

        NdefFormatable format = NdefFormatable.get(tag);
        if (format != null) {
            Log.d(TAG, "Write unformatted tag");
            try {
                format.connect();
                format.format(rawMessage);

                writeNdefSuccess();

                return true;
            } catch (Exception e) {
                writeNdefFailed(e);
            } finally {
                try {
                    format.close();
                } catch (IOException e) {
                    // ignore
                }
            }
            Log.d(TAG, "Cannot write unformatted tag");
        } else {
            Ndef ndef = Ndef.get(tag);
            if(ndef != null) {
                try {
                    Log.d(TAG, "Write formatted tag");

                    ndef.connect();
                    if (!ndef.isWritable()) {
                        Log.d(TAG, "Tag is not writeable");

                        writeNdefNotWritable();

                        return false;
                    }

                    if (ndef.getMaxSize() < rawMessage.toByteArray().length) {
                        Log.d(TAG, "Tag size is too small, have " + ndef.getMaxSize() + ", need " + rawMessage.toByteArray().length);

                        writeNdefTooSmall(rawMessage.toByteArray().length, ndef.getMaxSize());

                        return false;
                    }
                    ndef.writeNdefMessage(rawMessage);
                    //LOCK TAG
                    if (AppController.getInstance().isTagLock()){
                        ndef.makeReadOnly();
                    }
                    //END LOCK TAG

                    writeNdefSuccess();

                    return true;
                } catch (Exception e) {
                    writeNdefFailed(e);
                } finally {
                    try {
                        ndef.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            } else {
                writeNdefCannotWriteTech();
            }
            Log.d(TAG, "Cannot write formatted tag");
        }

        return false;
    }

    public int getMaxNdefSize(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        NdefFormatable format = NdefFormatable.get(tag);
        if (format != null) {
            Log.d(TAG, "Format tag with empty message");
            try {
                if(!format.isConnected()) {
                    format.connect();
                }
                format.format(new NdefMessage(new NdefRecord[0]));
            } catch (Exception e) {
                Log.d(TAG, "Problem checking tag size", e);

                return -1;
            }
        }

        Ndef ndef = Ndef.get(tag);
        if(ndef != null) {
            try {
                if(!ndef.isConnected()) {
                    ndef.connect();
                }

                if (!ndef.isWritable()) {
                    Log.d(TAG, "Capacity of non-writeable tag is zero");

                    writeNdefNotWritable();

                    return 0;
                }

                int maxSize = ndef.getMaxSize();

                ndef.close();

                return maxSize;
            } catch (Exception e) {
                Log.d(TAG, "Problem checking tag size", e);
            }
        } else {
            writeNdefCannotWriteTech();
        }
        Log.d(TAG, "Cannot get size of tag");

        return -1;
    }

    /**
     *
     * Create an NDEF message to be written when a tag is within range.
     *
     * @return the message to be written
     */

    protected abstract NdefMessage createNdefMessage();

    /**
     *
     * Writing NDEF message to tag failed.
     *
     * @param e exception
     */

    protected abstract void writeNdefFailed(Exception e);

    /**
     *
     * Tag is not writable or write-protected.
     *
     */

    protected abstract void writeNdefNotWritable();

    /**
     *
     * Tag capacity is lower than NDEF message size.
     *
     */

    protected abstract void writeNdefTooSmall(int required, int capacity);

    /**
     *
     * Unable to write this type of tag.
     *
     */

    protected abstract void writeNdefCannotWriteTech();

    /**
     *
     * Successfully wrote NDEF message to tag.
     *
     */

    protected abstract void writeNdefSuccess();

    /**
     *
     *
     */

    protected abstract boolean nfcFailAnimalIdCheck();

    @Override
    public void readNdefMessage(Message message) {
        if(message.size() > 1) {
            Log.d(TAG, getString(R.string.readMultipleRecordNDEFMessage));
        } else {
            Log.d(TAG, getString(R.string.readSingleRecordNDEFMessage));
        }

        this.message = message;

        // process message

        // show in log
        // iterate through all records in message
        Log.d(TAG, "Found " + message.size() + " NDEF records");
        String tagAnimalId = "";
        String tagSupervisor = null;
        String tagCountry = null;

        for(int k = 0; k < message.size(); k++) {
            Record record = message.get(k);

            Log.d(TAG, "Record " + k + " type " + record.getClass().getSimpleName());

            try {

                // your own code here, for example:
                if (record instanceof MimeRecord) {
                    MimeRecord mr = (MimeRecord) record;
                    if (mr.getMimeType().equals("cci/animalid")) {
                        tagAnimalId = new String(mr.getData(), "UTF-8");
                    }
                    if (mr.getMimeType().equals("cci/animaldispersalic")) {
                        tagSupervisor = new String(mr.getData(), "UTF-8");
                    }
                    if (mr.getMimeType().equals("cci/animalcountry")) {
                        tagCountry = new String(mr.getData(), "UTF-8");
                    }


                } else if (record instanceof ExternalTypeRecord) {
                    // ..
                } else if (record instanceof TextRecord) {
                    TextRecord tr = (TextRecord) record;
                    tagAnimalId = tr.getText();
                    // ..
                } else { // more else
                    // ..
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                Log.e(TAG, e.toString());
            }
        }
        readAnimalId = tagAnimalId;
    }


    /**
     * An empty NDEF message was read.
     *
     */

    @Override
    protected void readEmptyNdefMessage() {
        toast(getString(R.string.readEmptyMessage));

    }

    /**
     *
     * Something was read via NFC, but it was not an NDEF message.
     *
     * Handling this situation is out of scope of this project.
     *
     */

    @Override
    protected void readNonNdefMessage() {
        toast(getString(R.string.readNonNDEFMessage));
    }

    /**
     *
     * NFC feature was found and is currently enabled
     *
     */

    @Override
    protected void onNfcStateEnabled() {
        toast(getString(R.string.nfcAvailableEnabled));
    }

    /**
     *
     * NFC feature was found but is currently disabled
     *
     */

    @Override
    protected void onNfcStateDisabled() {
        toast(getString(R.string.nfcAvailableDisabled));
    }

    /**
     *
     * NFC setting changed since last check. For example, the user enabled NFC in the wireless settings.
     *
     */

    @Override
    protected void onNfcStateChange(boolean enabled) {
        if(enabled) {
            toast(getString(R.string.nfcAvailableEnabled));
        } else {
            toast(getString(R.string.nfcAvailableDisabled));
        }
    }

    /**
     *
     * This device does not have NFC hardware
     *
     */

    @Override
    protected void onNfcFeatureNotFound() {
        toast(getString(R.string.noNfcMessage));
    }

    /**
     *
     * Show NDEF records in the list
     *
     */

    private void showList() {
        // display the message
        // show in gui
        //ArrayAdapter<? extends Object> adapter = new NdefRecordAdapter(this, message);
        //ListView listView = (ListView) findViewById(R.id.recordListView);
        //listView.setAdapter(adapter);
        //listView.setVisibility(View.VISIBLE);
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


    protected void onTagLost() {
        //toast(getString(R.string.tagLost));
    }

}
