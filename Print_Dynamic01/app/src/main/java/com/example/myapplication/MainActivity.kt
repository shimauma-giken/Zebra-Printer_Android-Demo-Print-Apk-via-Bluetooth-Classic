package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zebra.sdk.comm.BluetoothConnectionInsecure
import com.zebra.sdk.comm.Connection
import com.zebra.sdk.printer.SGD


class MainActivity : AppCompatActivity() {
    // var theBtMacAddress = "CC:78:AB:68:1C:E2"
    //var theBtMacAddress = "48:A4:93:D9:4B:85"
    var theBtMacAddress = ""
    // val thePrinterConn: Connection = BluetoothConnectionInsecure(theBtMacAddress)
    var thePrinterConn: Connection = BluetoothConnectionInsecure(theBtMacAddress)
    //val zplData = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ"
    val zplDataDynamic = """
        ^XA
        ^DFE:dynamic.ZPL^FS
        ^PW600
        ^LL300
        ^LS0
        ^FPH,8^FT106,100^A0N,87,91^FH\^CI28^FN1"test01"^FS^CI27
        ^FT186,186^A0N,75,76^FH\^CI28^FN2"test02"^FS^CI27
        ^FT106,257^A0N,57,56^FH\^CI28^FN3"text03"^FS^CI27
        ^XZ
        
        ^XA
        ^XFE:dynamic.ZPL^FS
        ^CI28^FN1^FH\^FDA-27.2B^FS^CI27
        ^CI28^FN2^FH\^FDCYCLE 1^FS^CI27
        ^CI28^FN3^FH\^FDTBA234584895010^FS^CI27
        ^PQ1,0,1
        ^XZ
    """.trimIndent()

    val zplDataDynamicHead = """
        ^XA
        ^DFE:dynamic.ZPL^FS
        ^PW600
        ^LL300
        ^LS0
        ^FPH,8^FT106,100^A0N,87,91^FH\^CI28^FN1"test01"^FS^CI27
        ^FT186,186^A0N,75,76^FH\^CI28^FN2"test02"^FS^CI27
        ^FT106,257^A0N,57,56^FH\^CI28^FN3"text03"^FS^CI27
        ^XZ
        
        ^XA
        ^XFE:dynamic.ZPL^FS
        ^CI28^FN1^FH\^FDA-27.2B^FS^CI27
        ^CI28^FN2^FH\^FDCYCLE 1^FS^CI27
        ^CI28^FN3^FH\^FD
    """.trimIndent()
    val zplDataDynamicTail = """
        ^FS^CI27
        ^PQ1,0,1
        ^XZ
    """.trimIndent()

    val zplDataA = """^XA
    ~TA000
    ~JSN
    ^LT0
    ^MNN
    ^MTD
    ^PON
    ^PMN
    ^LH0,0
    ^JMA
    ^PR3,3
    ~SD10
    ^JUS
    ^LRN
    ^CI27
    ^PA0,1,1,0
    ^XZ
    ^XA
    ^MMT
    ^PW384
    ^LL1119
    ^LS0
    ^FT60,92^A@N,39,38,SGMTJ.TTF^FH\^CI28^FD123-45-67890-3^FS^CI27
    ^FT22,55^A@N,23,22,SGMTJ.TTF^FH\^CI28^FD* 追跡番号 (お問い合わせ番号) *^FS^CI27
    ^FT22,122^A@N,23,22,SGMTJ.TTF^FH\^CI28^FD(これは電話番号ではありません) ^FS^CI27
    ^FT38,192^A@N,55,20,SGMTJ.TTF^FH\^CI28^FD配達日 4/1 12:34   保管期限 4/8^FS^CI27
    ^FT29,296^A@N,23,22,SGMTJ.TTF^FH\^CI28^FD配達の^FS^CI27
    ^FT29,319^A@N,23,22,SGMTJ.TTF^FH\^CI28^FDお申し込みは^FS^CI27
    ^FT29,342^A@N,23,22,SGMTJ.TTF^FH\^CI28^FD右側のQRｺｰﾄﾞ^FS^CI27
    ^FT29,365^A@N,23,22,SGMTJ.TTF^FH\^CI28^FDからアクセス^FS^CI27
    ^FT29,388^A@N,23,22,SGMTJ.TTF^FH\^CI28^FDしてください。^FS^CI27
    ^FT219,404^BQN,2,4
    ^FH\^FDLA,https://trackings.post.japanpost.jp/delivery/sp/deli/^FS
    ^FT6,506^A@N,32,60,SGMTJ.TTF^FH\^CI28^FD(お客様控え)^FS^CI27
    ^FT12,553^A@N,25,26,SGMTJ.TTF^FH\^CI28^FD郵送にて再配達をされる^FS^CI27
    ^FT12,605^A@N,25,26,SGMTJ.TTF^FH\^CI28^FDお客様は必ず「お客様控え」を^FS^CI27
    ^FT12,657^A@N,25,26,SGMTJ.TTF^FH\^CI28^FDお手元に保管してください。^FS^CI27
    ^FT50,765^A@N,39,38,SGMTJ.TTF^FH\^CI28^FD123-45-67890-3^FS^CI27
    ^FT12,720^A@N,23,22,SGMTJ.TTF^FH\^CI28^FD* 追跡番号 (お問い合わせ番号) *^FS^CI27
    ^FT12,795^A@N,23,22,SGMTJ.TTF^FH\^CI28^FD(これは電話番号ではありません) ^FS^CI27
    ^FT12,841^A@N,23,22,SGMTJ.TTF^FH\^CI28^FDお届け日時    4月 1日  12時34分頃^FS^CI27
    ^FT12,869^A@N,23,22,SGMTJ.TTF^FH\^CI28^FD保管期限      4月 8日  まで^FS^CI27
    ^FT80,992^A@N,39,38,SGMTJ.TTF^FH\^CI28^FD123-45-67890-3^FS^CI27
    ^FT12,937^A@N,23,22,SGMTJ.TTF^FH\^CI28^FD* 追跡番号 (お問い合わせ番号) *^FS^CI27
    ^FT29,987^A@N,28,28,SGMTJ.TTF^FH\^CI28^FDツ^FS^CI27
    ^FO19,956^GE47,42,2^FS
    ^FT28,1057^A@N,55,20,SGMTJ.TTF^FH\^CI28^FD配達日 4/1 12:34   保管期限 4/8^FS^CI27
    ^FO6,441^GFA,313,1440,48,:Z64:eJzt08ENwjAMAECnqdI+KuDZB1IZIRuwSnjx7ZMfGS1sEokFGAAwtYMqBVRnAPCnjXqpnNgG+OXo08MtiwERNGKEM94BWoDOsLc9KM9i/rpG9B++yzyUfZ37puCbe+4pJcGrZ57PDWIl+cZkftgBGDGftz+x19HRDUi+UuyP7B8QqCKiR/aHSP5Cpx1Fv/cpf8v56+k2g3jeK/AZHLeGmt60F/9PJa2T3/DCyPUi0iVvebEteM896rhHyY9lb5MPtNBB9uAZu7QToy702+R12rKaJyT3X1Gl+WqXR+wfQrwAGJIonA==:4717
    ^FO6,881^GFA,313,1440,48,:Z64:eJzt08ENwjAMAECnqdI+KuDZB1IZIRuwSnjx7ZMfGS1sEokFGAAwtYMqBVRnAPCnjXqpnNgG+OXo08MtiwERNGKEM94BWoDOsLc9KM9i/rpG9B++yzyUfZ37puCbe+4pJcGrZ57PDWIl+cZkftgBGDGftz+x19HRDUi+UuyP7B8QqCKiR/aHSP5Cpx1Fv/cpf8v56+k2g3jeK/AZHLeGmt60F/9PJa2T3/DCyPUi0iVvebEteM896rhHyY9lb5MPtNBB9uAZu7QToy702+R12rKaJyT3X1Gl+WqXR+wfQrwAGJIonA==:4717
    ^PQ1,0,1,Y
    ^XZ
    """
    val zplData30 = """^XA
~TA000
~JSN
^LT0
^MNN
^MTD
^PON
^PMN
^LH0,0
^JMA
^PR3,3
~SD15
^JUS
^LRN
^CI27
^PA0,1,1,0
^XZ
^XA
^MMT
^PW384
^LL1119
^LS0
^FT0,44^A0N,29,25^FH\^CI28^FD01XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,80^A0N,29,25^FH\^CI28^FD02XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,116^A0N,29,25^FH\^CI28^FD03XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,152^A0N,29,25^FH\^CI28^FD04XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,188^A0N,29,25^FH\^CI28^FD05XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,224^A0N,29,25^FH\^CI28^FD06XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,260^A0N,29,25^FH\^CI28^FD07XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,296^A0N,29,25^FH\^CI28^FD08XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,332^A0N,29,25^FH\^CI28^FD09XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,368^A0N,29,25^FH\^CI28^FD10XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,404^A0N,29,25^FH\^CI28^FD11XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,440^A0N,29,25^FH\^CI28^FD12XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,476^A0N,29,25^FH\^CI28^FD13XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,512^A0N,29,25^FH\^CI28^FD14XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,548^A0N,29,25^FH\^CI28^FD15XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,584^A0N,29,25^FH\^CI28^FD16XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,620^A0N,29,25^FH\^CI28^FD17XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,656^A0N,29,25^FH\^CI28^FD18XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,692^A0N,29,25^FH\^CI28^FD19XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,728^A0N,29,25^FH\^CI28^FD20XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,764^A0N,29,25^FH\^CI28^FD21XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,800^A0N,29,25^FH\^CI28^FD22XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,836^A0N,29,25^FH\^CI28^FD23XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,872^A0N,29,25^FH\^CI28^FD24XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,908^A0N,29,25^FH\^CI28^FD25XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,944^A0N,29,25^FH\^CI28^FD26XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,980^A0N,29,25^FH\^CI28^FD27XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,1016^A0N,29,25^FH\^CI28^FD28XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,1052^A0N,29,25^FH\^CI28^FD29XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^FT0,1088^A0N,29,25^FH\^CI28^FD30XXXXXXXXXXXXXXXXXXXXXXXX^FS^CI27
^XZ"""


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //sendZplOverBluetooth(theBtMacAddress)
        // Instantiate insecure connection for given Bluetooth&reg; MAC Address.

        val btnOpen:    Button = findViewById(R.id.btnOpen)
        val btnSendA:    Button = findViewById(R.id.btnSendA)
        val btnClose:   Button = findViewById(R.id.btnClose)
        val tv01: TextView = findViewById(R.id.tv01)
        val et01: EditText = findViewById(R.id.et01)
        val etScan01: EditText = findViewById(R.id.etScan01)

        // et01.setText("48:A4:93:D9:4B:85" )
        //et01.setText("CC:78:AB:68:1C:C0" )

        //ZT411
        et01.setText("")

        // Bluetooth Open
        btnOpen.setOnClickListener {
            theBtMacAddress = et01.text.toString()
            thePrinterConn = BluetoothConnectionInsecure(theBtMacAddress)
            openBluetooth(thePrinterConn)
        }

        // Send ZPL Absent Label
        btnSendA.setOnClickListener { sendZpl(thePrinterConn, zplDataDynamic) }

        // Get Battery Remaining
        tv01.setOnClickListener {
            val powerRemainingP: String = SGD.GET("power.relative_state_of_charge", thePrinterConn)
            println("Hoge: $powerRemainingP ")
            tv01.text = "バッテリ残量: " + powerRemainingP
        }

        // Close Bluetooth
        btnClose.setOnClickListener { closeBluetooth(thePrinterConn) }


        etScan01.setOnEditorActionListener(OnEditorActionListener { v, actionId, event -> // TODO Auto-generated method stub
            Log.d("onEditorAction", "actionId = " + actionId + " event = " + (event ?: "null"))
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.action == KeyEvent.ACTION_UP) {
                    Log.d("onEditorAction", "check")
                    // ソフトキーボードを隠す
                    (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        v.windowToken,
                        0
                    )
                    Toast.makeText(applicationContext, etScan01.text.toString(), Toast.LENGTH_SHORT).show()
                    sendZpl(thePrinterConn, zplDataDynamicHead + etScan01.text.toString() + zplDataDynamicTail)
                    etScan01.setText("")

                }
                return@OnEditorActionListener true
            }
            false
        })

    }

    // 汎用オブジェクト

    fun openBluetooth(thePrinterConn: Connection) {
        Thread {
            try {

                // Initialize
                Looper.prepare()

                // Open the connection - physical connection is established here.
                thePrinterConn.open()

            } catch (e: Exception) {
                // Handle communications error here.
                e.printStackTrace()
            }
        }.start()
    }

    fun sendZpl(thePrinterConn: Connection, zplData: String) {
        Thread {
            try {
                // This example prints "This is a ZPL test." near the top of the label.

                // Send the data to printer as a byte array.
                thePrinterConn.write(zplData.toByteArray())

                // Make sure the data got to the printer before closing the connection
                Thread.sleep(500)

            } catch (e: Exception) {
                // Handle communications error here.
                e.printStackTrace()
            }
        }.start()
    }

    fun closeBluetooth(thePrinterConn: Connection) {
        Thread {
            try {
                // Close the insecure connection to release resources.
                thePrinterConn.close()
                Looper.myLooper()!!.quit()
            } catch (e: Exception) {
                // Handle communications error here.
                e.printStackTrace()
            }
        }.start()
    }

    fun sendZplOverBluetooth(theBtMacAddress: String) {
        Thread {
            try {
                // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
                val thePrinterConn: Connection =
                    BluetoothConnectionInsecure(theBtMacAddress)

                // Initialize
                Looper.prepare()

                // Open the connection - physical connection is established here.
                thePrinterConn.open()

                // This example prints "This is a ZPL test." near the top of the label.
                val zplData = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ"

                // Send the data to printer as a byte array.
                thePrinterConn.write(zplData.toByteArray())

                // Make sure the data got to the printer before closing the connection
                Thread.sleep(500)

                // Close the insecure connection to release resources.
                thePrinterConn.close()
                Looper.myLooper()!!.quit()
            } catch (e: Exception) {
                // Handle communications error here.
                e.printStackTrace()
            }
        }.start()
    }


}