package biz.markgo.senior_project.tracksharelocations.Database;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
        import android.os.Bundle;
        import android.app.Activity;
        import android.view.Menu;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.Toast;

import biz.markgo.senior_project.tracksharelocations.Database.ConnectServer;
import biz.markgo.senior_project.tracksharelocations.R;

public class testconnectionDBActivity extends Activity {

    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String> arrayAdapter;
    private ConnectServer connectServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testconnection_db);

        //เชื่อม listView กับ View
        listView = (ListView)findViewById(R.id.listView);

        //สร้างตัวเชื่อมต่อกับ Server ไปที่ URL ที่กำหนด
        connectServer = new ConnectServer(this, "http://senior-project.markgo.biz/member/getmember.php");

        //เพิ่มการส่งค่า age มีค่า 20 แบบ post
        connectServer.addValue("user_id","112220970771492250980");

        //เชื่อมต่อกับ Server
        connectServer.execute();
    }

    //ถ้าดึงข้อมูลจาก Server เสร็จแล้ว จะมาทำงานที่ Function นี้
    public void setList(ArrayList<String> list){
        this.list = list;
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, this.list);
        listView.setAdapter(arrayAdapter);
    }

    //ถ้าไม่สามารถเชื่อมต่อกับ Server ได้จะมาทำงานที่ Function นี้
    public void cannotConnectToServer() {
        Toast.makeText(this, "ไม่สามารถเชื่อมต่อกับ Server", Toast.LENGTH_LONG).show();
    }

    //ถ้าดึงข้อมูลจาก Server มีปัญหา จะมาทำงานที่ Function นี้
    public void errorConnectToServer() {
        Toast.makeText(this, "เกิดขอผิดพลาดในการดึงข้อมูล", Toast.LENGTH_LONG).show();
    }
}
