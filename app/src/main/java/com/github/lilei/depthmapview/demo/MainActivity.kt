package com.github.lilei.depthmapview.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.lilei.depthmapview.DepthBuySellData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buy =
            "[{\"amount\":\"516494.4900\",\"price\":\"0.000148\"},{\"amount\":\"10099.9999\",\"price\":\"0.000145\"},{\"amount\":\"28865.9722\",\"price\":\"0.000144\"},{\"amount\":\"1019275.5244\",\"price\":\"0.000143\"},{\"amount\":\"1243819.0139\",\"price\":\"0.000142\"},{\"amount\":\"2467081.9859\",\"price\":\"0.000141\"},{\"amount\":\"685328.5708\",\"price\":\"0.000140\"},{\"amount\":\"2384900.7191\",\"price\":\"0.000139\"},{\"amount\":\"210871.7389\",\"price\":\"0.000138\"},{\"amount\":\"17296.3502\",\"price\":\"0.000137\"},{\"amount\":\"123081.6173\",\"price\":\"0.000136\"},{\"amount\":\"861877.0363\",\"price\":\"0.000135\"},{\"amount\":\"598669.4028\",\"price\":\"0.000134\"},{\"amount\":\"887218.7968\",\"price\":\"0.000133\"},{\"amount\":\"2684262.8786\",\"price\":\"0.000132\"},{\"amount\":\"3122257.2515\",\"price\":\"0.000131\"},{\"amount\":\"612505.3840\",\"price\":\"0.000130\"},{\"amount\":\"71791.4728\",\"price\":\"0.000129\"},{\"amount\":\"650141.4061\",\"price\":\"0.000128\"},{\"amount\":\"454568.5038\",\"price\":\"0.000127\"},{\"amount\":\"3887016.6663\",\"price\":\"0.000126\"},{\"amount\":\"247112.8000\",\"price\":\"0.000125\"},{\"amount\":\"1572039.5160\",\"price\":\"0.000124\"},{\"amount\":\"898388.6176\",\"price\":\"0.000123\"},{\"amount\":\"30499.1802\",\"price\":\"0.000122\"},{\"amount\":\"203665.2889\",\"price\":\"0.000121\"},{\"amount\":\"482505.8332\",\"price\":\"0.000120\"},{\"amount\":\"19102.5209\",\"price\":\"0.000119\"},{\"amount\":\"1039101.6948\",\"price\":\"0.000118\"},{\"amount\":\"125602.5640\",\"price\":\"0.000117\"},{\"amount\":\"87967.2412\",\"price\":\"0.000116\"},{\"amount\":\"966166.0868\",\"price\":\"0.000115\"},{\"amount\":\"334629.8244\",\"price\":\"0.000114\"},{\"amount\":\"115053.9821\",\"price\":\"0.000113\"},{\"amount\":\"239043.7499\",\"price\":\"0.000112\"},{\"amount\":\"2481802.7026\",\"price\":\"0.000111\"},{\"amount\":\"396694.5453\",\"price\":\"0.000110\"},{\"amount\":\"8042.2018\",\"price\":\"0.000109\"},{\"amount\":\"45685.1850\",\"price\":\"0.000108\"},{\"amount\":\"276085.9811\",\"price\":\"0.000107\"},{\"amount\":\"1110069.8113\",\"price\":\"0.000106\"},{\"amount\":\"153936.1904\",\"price\":\"0.000105\"},{\"amount\":\"12069.2307\",\"price\":\"0.000104\"},{\"amount\":\"2097548.5436\",\"price\":\"0.000103\"},{\"amount\":\"1641847.0588\",\"price\":\"0.000102\"},{\"amount\":\"729310.8909\",\"price\":\"0.000101\"},{\"amount\":\"1708698.0000\",\"price\":\"0.000100\"},{\"amount\":\"2200.0000\",\"price\":\"0.000096\"},{\"amount\":\"99672.6315\",\"price\":\"0.000095\"},{\"amount\":\"606000.0000\",\"price\":\"0.000094\"}]"
        var sell =
            "[{\"amount\":\"18998.3492\",\"price\":\"0.000149\"},{\"amount\":\"579375.2636\",\"price\":\"0.000150\"},{\"amount\":\"10000.0000\",\"price\":\"0.000151\"},{\"amount\":\"23938.1907\",\"price\":\"0.000152\"},{\"amount\":\"39437.0592\",\"price\":\"0.000153\"},{\"amount\":\"70086.8975\",\"price\":\"0.000154\"},{\"amount\":\"119481.8449\",\"price\":\"0.000155\"},{\"amount\":\"31000.0000\",\"price\":\"0.000156\"},{\"amount\":\"398481.1022\",\"price\":\"0.000157\"},{\"amount\":\"1020826.4943\",\"price\":\"0.000158\"},{\"amount\":\"19700.0000\",\"price\":\"0.000159\"},{\"amount\":\"1540570.1143\",\"price\":\"0.000160\"},{\"amount\":\"510000.0000\",\"price\":\"0.000161\"},{\"amount\":\"42297.9030\",\"price\":\"0.000162\"},{\"amount\":\"905817.4543\",\"price\":\"0.000163\"},{\"amount\":\"320918.8569\",\"price\":\"0.000164\"},{\"amount\":\"85984.6779\",\"price\":\"0.000165\"},{\"amount\":\"849310.8825\",\"price\":\"0.000166\"},{\"amount\":\"337482.8245\",\"price\":\"0.000167\"},{\"amount\":\"1058834.3325\",\"price\":\"0.000168\"},{\"amount\":\"189803.0301\",\"price\":\"0.000169\"},{\"amount\":\"1010000.0000\",\"price\":\"0.000170\"},{\"amount\":\"553919.1734\",\"price\":\"0.000171\"},{\"amount\":\"103177.0000\",\"price\":\"0.000172\"},{\"amount\":\"69936.9942\",\"price\":\"0.000173\"},{\"amount\":\"112300.0984\",\"price\":\"0.000174\"},{\"amount\":\"89297.2116\",\"price\":\"0.000175\"},{\"amount\":\"224360.1818\",\"price\":\"0.000176\"},{\"amount\":\"539730.2447\",\"price\":\"0.000177\"},{\"amount\":\"30000.0000\",\"price\":\"0.000178\"},{\"amount\":\"25469.0000\",\"price\":\"0.000179\"},{\"amount\":\"1146947.3660\",\"price\":\"0.000180\"},{\"amount\":\"1000000.0000\",\"price\":\"0.000181\"},{\"amount\":\"523562.9441\",\"price\":\"0.000183\"},{\"amount\":\"2000.0000\",\"price\":\"0.000184\"},{\"amount\":\"876996.7708\",\"price\":\"0.000185\"},{\"amount\":\"558687.6729\",\"price\":\"0.000186\"},{\"amount\":\"570457.3663\",\"price\":\"0.000187\"},{\"amount\":\"1130000.0000\",\"price\":\"0.000188\"},{\"amount\":\"397615.3157\",\"price\":\"0.000190\"},{\"amount\":\"300000.0000\",\"price\":\"0.000191\"},{\"amount\":\"100000.0000\",\"price\":\"0.000192\"},{\"amount\":\"130077.7202\",\"price\":\"0.000193\"},{\"amount\":\"6257.9403\",\"price\":\"0.000194\"},{\"amount\":\"273440.9221\",\"price\":\"0.000195\"},{\"amount\":\"649800.0000\",\"price\":\"0.000196\"},{\"amount\":\"167367.9134\",\"price\":\"0.000197\"},{\"amount\":\"1109832.5267\",\"price\":\"0.000198\"},{\"amount\":\"30570.7676\",\"price\":\"0.000199\"},{\"amount\":\"3486373.7985\",\"price\":\"0.000200\"}]"

        var buyList: ArrayList<DepthBuySellData>? =
            parseGSON(buy, object : TypeToken<ArrayList<DepthBuySellData>>() {}.type)
        var sellList: ArrayList<DepthBuySellData>? =
            parseGSON(sell, object : TypeToken<ArrayList<DepthBuySellData>>() {}.type)

        if (buyList?.isNotEmpty()!! && sellList?.isEmpty()!!) {
            sellList.add(0, DepthBuySellData("0", "0"))
        } else if (buyList.isEmpty() && sellList?.isNotEmpty()!!) {
            buyList.add(0, DepthBuySellData("0", "0"))
        }
        depthMapView.setData(buyList, sellList, "BTC", 6, 4)
    }

    //根据 Type 解析
    fun <T> parseGSON(jsonString: String, typeOfT: Type): T? {
        var t: T? = null
        try {
            t = Gson().fromJson(jsonString, typeOfT)
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }

        return t
    }
}
