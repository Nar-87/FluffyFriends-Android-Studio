package com.example.fluffyfriends;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

//clase que sirve para la parte de las imagenes de perfil
public class FileUtils {

    //de momento está clase no se está usando, la mantengo por si acasa en algún día quiero modificar algo en las imagenes

    public static String getPath(Context context, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver()
                .query(uri, projection, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }

        return null;
    }
}

