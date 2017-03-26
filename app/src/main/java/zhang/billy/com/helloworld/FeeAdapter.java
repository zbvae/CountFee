package zhang.billy.com.helloworld;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhangvae on 2017/3/26.
 */

public class FeeAdapter extends BaseAdapter {

    private Context context;
    private List<PersonFee> persons;

    public FeeAdapter(Context context, List<PersonFee> persons) {
        this.context = context;
        this.persons = persons;
    }

    public FeeAdapter() {
        super();
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //观察convertView随ListView滚动情况
        Log.v("FeeAdapter", "getView " + position + " " + convertView);
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem, null);
            holder = new ViewHolder();
            holder.feeDate = (TextView)convertView.findViewById(R.id.fee_date);
            holder.feeItem1 = (TextView)convertView.findViewById(R.id.fee1_item);
            holder.feeItem2 = (TextView)convertView.findViewById(R.id.fee2_item);
            holder.feeItem3 = (TextView)convertView.findViewById(R.id.fee3_item);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PersonFee person = persons.get(position);
        holder.feeDate.setText(person.getCreatetime()+"        ");// 日期后面空隙
        holder.feeItem1.setText(new StringBuilder().append("1号房间用电量：").append(person.getEle1()).append(" 电费：").append(person.getFee1()).toString());
        holder.feeItem2.setText(new StringBuilder().append("2号房间用电量：").append(person.getEle2()).append(" 电费：").append(person.getFee2()).toString());
        holder.feeItem3.setText(new StringBuilder().append("3号房间用电量：").append(person.getEle3()).append(" 电费：").append(person.getFee3()).toString());

        return convertView;
    }

    class ViewHolder {
        TextView feeDate;
        TextView feeItem1;
        TextView feeItem2;
        TextView feeItem3;
    }
}
