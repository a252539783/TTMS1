package com.example.a38938.ttms1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a38938.ttms1.Adapter.PlayManageAdapter;
import com.example.a38938.ttms1.data.PlayData;
import com.example.a38938.ttms1.store.OnDataGetListener;
import com.example.a38938.ttms1.store.StoreBusiness;
import com.example.a38938.ttms1.store.StoreManager;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by LQF on 2018/5/29.
 */

public class PlayManageFragment extends MFragment implements View.OnClickListener {

    private ViewGroup mView = null;
    private ViewGroup mActionBar = null;

    private RecyclerView mList = null;
    private PlayManageAdapter mAdapter = new PlayManageAdapter(this);
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<PlayData> datas = (List<PlayData>) msg.obj;
            mAdapter.setData(datas);
        }
    };
    private OnDataGetListener<PlayData> mGetListener = new OnDataGetListener<PlayData>() {
        @Override
        public void onReceive(List<PlayData> datas) {
            mHandler.sendMessage(mHandler.obtainMessage(0, datas));
        }
    };

    private ImageView mDeleteButton, mSelectButton, mAddButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.play_manage, container, false);
            mActionBar = (ViewGroup)inflater.inflate(R.layout.play_manage_actionbar, container, false);
        }

        initView();
        return mView;
    }

    @Override
    public boolean onBackPressed() {
        if (mAdapter.selecting()) {
            mAdapter.cancelSelect();
            return true;
        }

        return false;
    }

    public void showDelete() {
        mAddButton.setVisibility(View.GONE);
        mDeleteButton.setVisibility(View.VISIBLE);
        mSelectButton.setVisibility(View.VISIBLE);
    }

    public void hideDelete() {
        mAddButton.setVisibility(View.VISIBLE);
        mDeleteButton.setVisibility(View.GONE);
        mSelectButton.setVisibility(View.GONE);
    }

    private void initView() {
        if (mList == null) {
            mList = mView.findViewById(R.id.play_list);
            mList.setLayoutManager(new LinearLayoutManager(getContext()));
            mList.setAdapter(mAdapter);
            mDeleteButton = mActionBar.findViewById(R.id.play_action_delete);
            mSelectButton = mActionBar.findViewById(R.id.play_action_select);
            mAddButton = mActionBar.findViewById(R.id.play_action_add);
            mAddButton.setOnClickListener(this);
            mSelectButton.setOnClickListener(this);
            mDeleteButton.setOnClickListener(this);
        }

        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(mActionBar);
        StoreManager.get().getAllDatas(PlayData.class, mGetListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        if (v == mAddButton) {
            edit(new PlayData());
        } else if (v == mDeleteButton) {
            showDeleteDialog();
        } else if (v == mSelectButton) {
            mAdapter.selectAll();
        } else if (v == mSelectImgContainer) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
        } else if (v == mSave) {
            mDialog.dismiss();
            mCurrentEditData.length = Integer.decode(mEditLength.getText().toString());
            mCurrentEditData.actors = mEditActor.getText().toString();
            mCurrentEditData.director = mEditDirector.getText().toString();
            mCurrentEditData.intro = mEditIntro.getText().toString();
            mCurrentEditData.score = Float.parseFloat(mEditScore.getText().toString());
            mCurrentEditData.name = mEditName.getText().toString();

            if (mCurrentEditData.id == -1) {
                StoreManager.get().insert(mCurrentEditData);
                mAdapter.getDatas().add(mCurrentEditData);
                mAdapter.notifyDataSetChanged();
            } else {
                StoreManager.get().update(mCurrentEditData);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 编辑，传入一个data的id为-1表示添加
     * @param data
     */
    public void edit(PlayData data) {
        String oldPath = null;
        if (mCurrentEditData != null) {
            oldPath = mCurrentEditData.imgPath;
        }
        mCurrentEditData = data;
        if (data.id == -1) {    //对于新添加的先保留上次编辑的内容
            mCurrentEditData.imgPath = oldPath;
        }
        showEditDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                mImg.setImageURI(uri);

                if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                    mCurrentEditData.imgPath = uri.getPath();
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                    mCurrentEditData.imgPath = getPath(getContext(), uri);
                } else {
                    mCurrentEditData.imgPath = getRealPathFromURI(uri);
                }
                mSelectImg.setBackgroundColor(Color.TRANSPARENT);
                mSelectImg.setText("");
            }
        }
    }

    private PlayData mCurrentEditData = null;
    private AppCompatDialog mDialog;

    private EditText mEditName, mEditDirector, mEditActor, mEditScore, mEditIntro, mEditLength;
    private TextView mSelectImg;
    private ImageView mImg;
    private View mSelectImgContainer;
    private Button mSave;
    public void showEditDialog() {
        if (mDialog == null) {
            mDialog = new AppCompatDialog(getContext());
            mDialog.setContentView(R.layout.play_modify);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(true);

            mEditName = mDialog.findViewById(R.id.play_edit_e_name);
            mEditDirector = mDialog.findViewById(R.id.play_edit_e_director);
            mEditActor = mDialog.findViewById(R.id.play_edit_e_actor);
            mEditScore = mDialog.findViewById(R.id.play_edit_e_score);
            mEditIntro = mDialog.findViewById(R.id.play_edit_e_intro);
            mEditLength = mDialog.findViewById(R.id.play_edit_e_length);
            mSave = mDialog.findViewById(R.id.play_edit_b_save);
            mImg = mDialog.findViewById(R.id.play_edit_img);
            mSelectImgContainer = mDialog.findViewById(R.id.play_edit_img_select_container);
            mSelectImg = mDialog.findViewById(R.id.play_edit_img_select);
            mSelectImgContainer.setOnClickListener(this);
            mSave.setOnClickListener(this);
        }

        updateDialog();
        mDialog.show();
    }

    private void updateDialog() {
        if (mCurrentEditData != null && mCurrentEditData.id != -1) {
            mEditName.setText(mCurrentEditData.name);
            mEditActor.setText(mCurrentEditData.actors);
            mEditDirector.setText(mCurrentEditData.director);
            mEditIntro.setText(mCurrentEditData.intro);
            mEditLength.setText(String.valueOf(mCurrentEditData.length));
            mEditScore.setText(String.valueOf(mCurrentEditData.score));

            if (mCurrentEditData.imgPath != null) {
                mImg.setImageURI(Uri.fromFile(new File(mCurrentEditData.imgPath)));
                mSelectImg.setBackgroundColor(Color.TRANSPARENT);
                mSelectImg.setText("");
            } else {
                mImg.setImageURI(null);
                mSelectImg.setBackgroundResource(R.drawable.select_text_bg);
                mSelectImg.setText("选择图片");
            }
        }
    }

    private AlertDialog mDeleteDialog;
    private void showDeleteDialog() {
        if (mDeleteDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            mDeleteDialog = builder.setTitle("确认是否删除?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDeleteDialog.dismiss();
                            mAdapter.delete();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDeleteDialog.dismiss();
                        }
                    })
                    .setCancelable(true)
                    .show();
        } else {
            mDeleteDialog.show();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if(null!=cursor&&cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
