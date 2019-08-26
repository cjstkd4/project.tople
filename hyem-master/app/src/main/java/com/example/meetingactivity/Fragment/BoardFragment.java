package com.example.meetingactivity.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetingactivity.R;
import com.example.meetingactivity.adapter.BoardAdapter;
import com.example.meetingactivity.adapter.ShowAdapter;
import com.example.meetingactivity.model.Board;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFragment extends Fragment implements View.OnClickListener {

    ImageButton noticeButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Animation fab_open,fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    LinearLayout fabLayout,fabLayout1,fabLayout2,boardLayout;
    LinearLayout inputLayout;
    TextView textFab1,textFab2;
    Button bntBack, bntWrite;
    EditText editTitle,editContent;
    ListView listNotice;
    ArrayList<Board> list;
    BoardAdapter adapter;
    ShowAdapter adapter2;
    //통신용 객체 선언
    AsyncHttpClient client;
    HttpResponse response;

    String URL= "http://192.168.0.64:8080/0823/board/board_insert.jsp";

    String URLlist= "http://192.168.0.64:8080/0823/board/board_list.jsp";




    public BoardFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BoardFragment newInstance(String param1, String param2) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        fab_open= AnimationUtils.loadAnimation(getActivity(),R.anim.fab_open);
        fab_close=AnimationUtils.loadAnimation(getActivity(),R.anim.fab_close);

        client=new AsyncHttpClient();
        response= new HttpResponse(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view =inflater.inflate(R.layout.fragment_board, container, false);
        //플로팅 액션버튼
        fab=(FloatingActionButton) view.findViewById(R.id.fab);
        fab1=(FloatingActionButton)view.findViewById(R.id.fab1);
        fab2=(FloatingActionButton)view.findViewById(R.id.fab2);

        textFab1=view.findViewById(R.id.textFab1);
        textFab2=view.findViewById(R.id.textFab2);

        fabLayout=view.findViewById(R.id.fabLayout);
        fabLayout1=view.findViewById(R.id.fabLayout1);
        fabLayout2=view.findViewById(R.id.fabLayout2);

        boardLayout=view.findViewById(R.id.boardLayout);
        inputLayout=view.findViewById(R.id.inputLayout);



        //공지사항 입력 후 리스트에 추가
        listNotice=view.findViewById(R.id.listNotice);
        list = new ArrayList<>();
        adapter=new BoardAdapter(getActivity(),R.layout.list_notice,list);
        adapter2=new ShowAdapter(getActivity(),R.layout.list_item,list);

        listNotice.setAdapter(adapter);
       listNotice.setAdapter(adapter2);



        bntBack=view.findViewById(R.id.bntBack);
        bntWrite=view.findViewById(R.id.bntWrite);

        editTitle=view.findViewById(R.id.editTitle);
        editContent=view.findViewById(R.id.editContent);

        bntBack.setOnClickListener(this);
        bntWrite.setOnClickListener(this);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter2.clear();
        client.post(URLlist,response);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    //이벤트설정
    @Override
    public void onClick(View v) {


        Intent intent = new Intent();
       intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        switch (v.getId()){
            //플로팅 액션 버튼(+) 눌렀을때
            case R.id.fab:
                anim();
                break;
            case R.id.fab1://일반게시글 입력화면으로
                anim();
                boardLayout.setVisibility(View.GONE);
                fabLayout.setVisibility(View.GONE);
                inputLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.fab2: //공지사항 입력화면으로  //+모임장 또는 관리자 권한을 가진사람만 보여주게해야함
                anim();
                Toast.makeText(getActivity(),"button2",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bntBack: //돌아가기 버튼
                editTitle.setText("");
                editContent.setText(""); //입력내용초기화
                inputLayout.setVisibility(View.GONE);
                boardLayout.setVisibility(View.VISIBLE);
                fabLayout.setVisibility(View.VISIBLE);
                break;
            case  R.id.bntWrite:
                String subject=editTitle.getText().toString().trim();
                String content=editContent.getText().toString().trim();
                String id="1";
                String moimcode="1";
                String filename="1";
                String listnum="2";
                String thumb="4";
                String editdate="2";
                String lev = "4";

                Board item = new Board();
                item.setSubject(subject);
                item.setContent(content);
                list.add(item);

                //입력값이 있으면, 서버로 데이터 전송 및 요청
                RequestParams params = new RequestParams();

                params.put("id",id);
                params.put("subject",subject);
                params.put("content",content);
                params.put("moimcode",moimcode);
                params.put("filename",filename);
                params.put("listnum",listnum);
                params.put("thumb",thumb);
                params.put("editdate",editdate);
                params.put("lev",lev);
              //  params.put();

                client.post(URL,params,response);


                editTitle.setText("");
                editContent.setText("");
                inputLayout.setVisibility(View.GONE);
                boardLayout.setVisibility(View.VISIBLE);
                fabLayout.setVisibility(View.VISIBLE);
                break;

        }
    }


    //플로팅액션버튼 동작 함수
    public void anim() {
                if (isFabOpen) {
                        fab.animate().rotationBy(60);
                        fab1.startAnimation(fab_close);
                        fab2.startAnimation(fab_close);
                        fabLayout1.setVisibility(View.GONE);
                        fabLayout2.setVisibility(View.GONE);
                        fab1.setClickable(false);
                        fab2.setClickable(false);
                         isFabOpen = false;
                     } else {
                    fab.animate().rotationBy(-60);

                         fab1.startAnimation(fab_open);
                         fab2.startAnimation(fab_open);
                        fabLayout1.setVisibility(View.VISIBLE);
                        fabLayout2.setVisibility(View.VISIBLE);
                         fab1.setClickable(true);
                         fab2.setClickable(true);
                         isFabOpen = true;
                     }
             }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public HttpResponse(Activity activity) {
            this.activity = activity;
        }

        //통신 시작
        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려 주세요...");
            dialog.setCancelable(false);
            dialog.show();
        }
        // 통신 종료
        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }
        //통신 성공
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt  = json.getString("rt");
                if(rt.equals("OK")){
                    Toast.makeText(activity,"저장성공",Toast.LENGTH_SHORT).show();

                                    }else {
                    Toast.makeText(activity,"실패",Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //통신 실패
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패"+statusCode, Toast.LENGTH_SHORT).show();
        }
    }
}
