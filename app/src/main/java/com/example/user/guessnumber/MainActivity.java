package com.example.user.guessnumber;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.user.guessnumber.adapter.MyAdapter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.ListItemClickListener {

    private EditText edtNumRange;
    private TextInputLayout numRangeLayout;
    private ImageButton imgBtnReset;
    private RecyclerView recyclerView;

    public static int answer;
    private ArrayList<Boolean> isInRange = new ArrayList<Boolean>();
    private MyAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find view
        edtNumRange = (EditText) findViewById(R.id.edtNumRange);
        numRangeLayout = (TextInputLayout) findViewById(R.id.numRangeLayout);
        imgBtnReset = (ImageButton) findViewById(R.id.imgBtnReset);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        imgBtnReset.setOnClickListener(imgBtnResetListener);

        //拿回螢幕旋轉而流失的資料
        if (savedInstanceState != null){
            boolean[] data = savedInstanceState.getBooleanArray("data");
            for(int i = 0; i < data.length; i++){
                isInRange.add(data[i]);
            }
            adapter = new MyAdapter(isInRange, MainActivity.this);
            LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(MainActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putBooleanArray("data",toPrimitiveArray(isInRange));

        super.onSaveInstanceState(outState);
    }

    ImageButton.OnClickListener imgBtnResetListener = new ImageButton.OnClickListener() {
        @Override public void onClick(View view) {
            int range;
            isInRange.clear();

            //確保使用者有輸入值，且為數字
            if (edtNumRange != null || edtNumRange.length() != 0) {
                try {
                    range = Integer.valueOf(edtNumRange.getText().toString());
                    answer = (int) (Math.random() * range + 1);

                    for (int i = 0; i < range; i++) {
                        isInRange.add(true);
                    }

                    adapter = new MyAdapter(isInRange, MainActivity.this);
                    LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(MainActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Log.e("imgBtnResetListener", e.toString());
                    numRangeLayout.setError("請輸入數字哦!");
                }
            } else {
                numRangeLayout.setError("請輸入數字哦!");
            }
        }
    };

    @Override public void onListItemClickListener(int clickItemIndex) {
        //猜到就把全部變成false，背景就會變灰色的
        if (clickItemIndex + 1 == answer) {
            Toast.makeText(MainActivity.this, answer + "就是答案哦!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < isInRange.size(); i++) {
                isInRange.set(i, false);
            }
        }

        //超出範圍就變成false，讓背景變成灰色
        if (clickItemIndex + 1 > answer) {
            for (int i = isInRange.size() - 1; i >= clickItemIndex; i--) {
                isInRange.set(i, false);
            }
        } else {
            for (int i = 0; i < clickItemIndex + 1; i++) {
                isInRange.set(i, false);
            }
        }
        //通知資料已修改
        adapter.notifyDataSetChanged();
    }

    private boolean[] toPrimitiveArray(final List<Boolean> booleanList) {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList) {
            primitives[index++] = object;
        }
        return primitives;
    }
}
