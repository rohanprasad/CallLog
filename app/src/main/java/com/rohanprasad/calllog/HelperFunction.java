package com.rohanprasad.calllog;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.rohanprasad.calllog.holder.CallLogItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Rohan on 11-06-2015.
 */
public class HelperFunction {

    public static String getTitleCase(String input) {

        String titleCase = "";
        String[] parts = input.split(" ");

        for (int i = 0; i < parts.length; i++) {
            titleCase += Character.toUpperCase(parts[i].charAt(0)) + parts[i].substring(1).toLowerCase();

            if (i != parts.length - 1) {
                titleCase += " ";
            }
        }

        return titleCase;
    }

    public static String getTimeFormat(String input) {

        Date mNow = new Date(Long.valueOf(System.currentTimeMillis()));
        Date mDate = new Date(Long.valueOf(input));

        long diff = mNow.getTime() - mDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 1) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
            return dateFormat.format(mDate).toString();
        } else if (days > 0) {
            return "Yesterday";
        } else if (hours > 0) {
            if (hours > 1)
                return (hours + " hrs ago");
            else
                return (hours + " hr ago");
        } else if (minutes > 0) {
            if (minutes > 1)
                return (minutes + " mins ago");
            else
                return (minutes + " min ago");
        } else {
            if (seconds > 1)
                return (seconds + " secs ago");
            else if (seconds == 1)
                return (seconds + " sec ago");
            else
                return ("Just now");
        }
    }

    public static String getDurationFormat(String input) {

        long num = Long.parseLong(input);
        int sec = (int) (num % 60);
        int min = (int) ((num / 60) % (60));
        int hrs = (int) ((num / 3600) % (60));

        String res = "";

        if (hrs > 0) {
            if (hrs > 10)
                res += hrs + ":";
            else
                res += "0" + hrs + ":";
        }

        if (min > 10)
            res += min + ":";
        else
            res += "0" + min + ":";

        if (sec > 10)
            res += sec;
        else
            res += "0" + sec;

        return res;
    }

    public static Long getThumbnailId(Context context, String phoneNumber) {

        final Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        final Cursor cursor = context.getContentResolver()
                .query(uri, new String[]{ContactsContract.Contacts.PHOTO_ID}, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        try {
            Long thumbnailId = null;
            if (cursor.moveToFirst()) {
                thumbnailId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
            }
            return thumbnailId;
        } finally {
            cursor.close();
        }
    }

    public static Bitmap getThumbnail(Context context, Long thumbnailId) {

        final Uri uri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, thumbnailId);
        final Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);

        try {
            Bitmap thumbnail = null;
            if (cursor.moveToFirst()) {
                final byte[] thumbnailBytes = cursor.getBlob(0);
                if (thumbnailBytes != null) {
                    thumbnail = BitmapFactory.decodeByteArray(thumbnailBytes, 0, thumbnailBytes.length);
                }
            }
            return thumbnail;
        } finally {
            cursor.close();
        }
    }

    public static String getContactID(Context context, String phoneNumber) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                context.getContentResolver().query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        String contactId = null;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        return contactId;
    }


    public static Uri getPhotoUri(Context context, String contactId) {

        if (contactId != null && contactId.length() > 0) {
            Uri person = ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
            Uri photo = Uri.withAppendedPath(person,
                    ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

            Cursor cur = context
                    .getContentResolver()
                    .query(
                            ContactsContract.Data.CONTENT_URI,
                            null,
                            ContactsContract.Data.CONTACT_ID
                                    + "="
                                    + contactId
                                    + " AND "
                                    + ContactsContract.Data.MIMETYPE
                                    + "='"
                                    + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                    + "'", null, null);
            if (cur != null) {
                if (!cur.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }
            return photo;
        }

        return null;
    }

    public static int getAlphabetTile(String name) {

        int[] alphabets = {
                R.drawable.alphabet_a,
                R.drawable.alphabet_b,
                R.drawable.alphabet_c,
                R.drawable.alphabet_d,
                R.drawable.alphabet_e,
                R.drawable.alphabet_f,
                R.drawable.alphabet_g,
                R.drawable.alphabet_h,
                R.drawable.alphabet_i,
                R.drawable.alphabet_j,
                R.drawable.alphabet_k,
                R.drawable.alphabet_l,
                R.drawable.alphabet_m,
                R.drawable.alphabet_n,
                R.drawable.alphabet_o,
                R.drawable.alphabet_p,
                R.drawable.alphabet_q,
                R.drawable.alphabet_r,
                R.drawable.alphabet_s,
                R.drawable.alphabet_t,
                R.drawable.alphabet_u,
                R.drawable.alphabet_v,
                R.drawable.alphabet_w,
                R.drawable.alphabet_x,
                R.drawable.alphabet_y,
                R.drawable.alphabet_z
        };

        if (name != null && name.length() > 0 && Character.isLetter(name.charAt(0)))
            return alphabets[Character.toLowerCase(name.charAt(0)) - 'a'];
        else
            return R.drawable.user;
    }


    public static ArrayList<CallLogItem> sortUsingDate(ArrayList<CallLogItem> mDataList) {
        Collections.sort(mDataList, new Comparator<CallLogItem>() {
            @Override
            public int compare(CallLogItem result1, CallLogItem result2) {
                return result1.getDate().compareTo(result2.getDate());
            }
        });

        Collections.reverse(mDataList);
        return mDataList;
    }

    public static ArrayList<CallLogItem> sortUsingDuration(ArrayList<CallLogItem> mDataList) {

        Collections.sort(mDataList, new Comparator<CallLogItem>() {
            @Override
            public int compare(CallLogItem result1, CallLogItem result2) {

                int comp = LongCompare(result1.getDuration(), result1.getDuration());

                if(comp == 0){
                    if (Character.isLetter(result1.getName().charAt(0)) && Character.isLetter(result2.getName().charAt(0))) {
                        return result1.getName().compareToIgnoreCase(result2.getName());
                    } else {
                        if (Character.isLetter(result1.getName().charAt(0))) {
                            return -1;
                        } else if (Character.isLetter(result2.getName().charAt(0))) {
                            return 1;
                        } else {
                            return result1.getName().compareToIgnoreCase(result2.getName());
                        }
                    }
                }else{
                    return comp;
                }
            }
        });

        Collections.reverse(mDataList);
        return mDataList;
    }

    public static ArrayList<CallLogItem> sortUsingUser(ArrayList<CallLogItem> mDataList) {
        Collections.sort(mDataList, new Comparator<CallLogItem>() {
            @Override
            public int compare(CallLogItem result1, CallLogItem result2) {

                if (Character.isLetter(result1.getName().charAt(0)) && Character.isLetter(result2.getName().charAt(0))) {
                    return result1.getName().compareToIgnoreCase(result2.getName());
                } else {
                    if (Character.isLetter(result1.getName().charAt(0))) {
                        return -1;
                    } else if (Character.isLetter(result2.getName().charAt(0))) {
                        return 1;
                    } else {
                        return result1.getName().compareToIgnoreCase(result2.getName());
                    }
                }

            }
        });


        return mDataList;
    }

    public static int LongCompare(Long x, Long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

}
