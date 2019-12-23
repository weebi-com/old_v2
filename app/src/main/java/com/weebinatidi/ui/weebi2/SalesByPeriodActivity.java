package com.weebinatidi.ui.weebi2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.model.LaReffb;
import com.weebinatidi.ui.BaseActivity;
import com.weebinatidi.ui.weebi2.adapters.SalesByDayRefAdapter;
import com.weebinatidi.utils.DateUtils;
import com.weebinatidi.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * @author Birante SY (birantesy@gmail.com)
 *         <p>
 *         Cette classe nous permet
 *         d'avoir des analyses Journalieres sur les ventes.
 *         <p>Activity to see sales by day<p>
 *         cette activité est utilisée pour afficher le montant total des ventes
 *         dans un intervalle de jour et les détails des articles vendu (quantité,nom,image).
 */


public class SalesByPeriodActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    static String startTime;
    static String endTime;
    DbHelper dbHelper;
    private TextView tvDay;
    private TextView tvDayEnd;
    private TextView tvAmount;
    private TextView tvDailyAmount;
    private Button dateButton;
    private TextView tv_text;
    private TextView tv_text2;
    private TextView tvHourAndMinute;
    private TextView tvHourAndMinuteEnd;
    private Button timeButton;


    private TextView mTotalCashTextView;
    private TextView mTotalCreditTextView;

    String startDate;
    String endDate;
    int totalSalesAmount;

    private ListView listView;
    ArrayList<LaReffb> references;

    int itemMaxQty;

    int montantTotalACredit;
    int montantTotalEnCashClient;
    int montantTotalEnCash;
    int montantTotalCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        closeDelfaultKeyboard();
        initView();

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                SalesByPeriodActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        SalesByPeriodActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        SalesByPeriodActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

    }


    public void initView() {
        tvDay = findViewById(R.id.tv_day);
        tvDayEnd = findViewById(R.id.tv_day_end);
        tvAmount = findViewById(R.id.tv_amount);
        tvDailyAmount = findViewById(R.id.tv_daily_amount);

        timeButton = findViewById(R.id.btn_time);
        dateButton = findViewById(R.id.btn_date);

        tvHourAndMinute = findViewById(R.id.tv_hour_and_minute);
        tvHourAndMinuteEnd = findViewById(R.id.tv_hour_and_minute_end);

        tv_text = findViewById(R.id.tv_text);
        tv_text2 = findViewById(R.id.tv_text2);

        mTotalCashTextView = findViewById(R.id.tv_cash_sales);
        mTotalCreditTextView = findViewById(R.id.tv_credit_sales);
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_sales_by_period;
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        clearTimeUI();
        dbHelper = new DbHelper(this);
        String dayString = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String dayStringEnd = dayOfMonthEnd < 10 ? "0" + dayOfMonthEnd : "" + dayOfMonthEnd;
        String monthString = monthOfYear < 10 ? "0" + (++monthOfYear) : "" + monthOfYear;
        String monthStringEnd = monthOfYearEnd < 10 ? "0" + (++monthOfYearEnd) : "" + monthOfYearEnd;

        tvDay.setText(dayString + "-" + monthString + "-" + String.valueOf(year));
        tvDayEnd.setText(dayOfMonthEnd + "-" + monthStringEnd + "-" + String.valueOf(yearEnd));

        // Get the string value of these selected date.
        startDate = String.valueOf(year) + "-" + monthString + "-" + dayString;
        endDate = String.valueOf(yearEnd) + "-" + monthStringEnd + "-" + dayStringEnd;

        if (startDate.equals(endDate)) {
            totalSalesAmount = dbHelper.getTotalAmountOfInvoiceInThatDay(startDate);
            tvDailyAmount.setText(Config.formaterSolde(totalSalesAmount));
            references = dbHelper.getSalesListOfTheDay(startDate);
            itemMaxQty = dbHelper.getMaxOfItemsQuantity(startDate); // Recupere le maximun des quantites


            montantTotalACredit = dbHelper.getTotalCreditAmountOfInvoiceInThatDay(startDate);
            montantTotalEnCashClient = dbHelper.getTotalCashClientAmountOfInvoiceInThatDay(startDate);
            montantTotalEnCash = dbHelper.getTotalCashAmountOfInvoiceInThatDay(startDate);
            montantTotalCash = montantTotalEnCash + montantTotalEnCashClient;


            mTotalCreditTextView.setText(Config.formaterSolde(montantTotalACredit));
            mTotalCashTextView.setText(Config.formaterSolde(montantTotalCash));

        } else {
            totalSalesAmount = dbHelper.getTotalAmountOfInvoice(startDate, endDate);
            tvDailyAmount.setText(Config.formaterSolde(totalSalesAmount));
            references = dbHelper.getTheListOfSalesForAPeriod(startDate, endDate);
            itemMaxQty = dbHelper.getMaxOfItemsQuantity(startDate, endDate); // Recupere le maximun des quantites


            montantTotalACredit = dbHelper.getTotalCreditAmountOfInvoiceInThatDay(startDate, endDate);
            montantTotalEnCashClient = dbHelper.getTotalCashClientAmountOfInvoiceInThatDay(startDate, endDate);
            montantTotalEnCash = dbHelper.getTotalCashAmountOfInvoiceInThatDay(startDate, endDate);
            montantTotalCash = montantTotalEnCash + montantTotalEnCashClient;


            mTotalCreditTextView.setText(Config.formaterSolde(montantTotalACredit));
            mTotalCashTextView.setText(Config.formaterSolde(montantTotalCash));
        }


        SalesByDayRefAdapter mDayRefAdapter = new SalesByDayRefAdapter(this, references, itemMaxQty);
        listView = findViewById(R.id.lv_ref);
        listView.setAdapter(mDayRefAdapter);
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

        listView.setAdapter(null);
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String hourStringEnd = hourOfDayEnd < 10 ? "0" + hourOfDayEnd : "" + hourOfDayEnd;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String minuteStringEnd = minuteEnd < 10 ? "0" + minuteEnd : "" + minuteEnd;
        tvHourAndMinute.setText(hourString + ":" + minuteString);
        tvHourAndMinuteEnd.setText(hourStringEnd + ":" + minuteStringEnd);

        if (StringUtils.isNullOrBlank(startDate) && StringUtils.isNullOrBlank(endDate)) {
            LayoutInflater inflater = getLayoutInflater();
            View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(toastLayout);
            toast.show();
        } else {

            startTime = DateUtils.assembleTime(hourString, minuteString);
            endTime = DateUtils.assembleTime(hourStringEnd, minuteStringEnd);

            dbHelper = new DbHelper(this);
            int total = dbHelper.getTotalAmountOfInvoiceWithTime(startTime, endTime, startDate, endDate);
            tvAmount.setText(Config.formaterSolde(Integer.parseInt(String.valueOf(total))));

            ArrayList<LaReffb> productList = dbHelper.getTheListOfSalesForAPeriodWithTime(startTime, endTime, startDate, endDate);

            montantTotalACredit = dbHelper.getTotalCreditAmountOfInvoiceInThatDay(startTime, endTime, startDate, endDate);
            montantTotalEnCashClient = dbHelper.getTotalCashClientAmountOfInvoiceInThatDay(startTime, endTime, startDate, endDate);
            montantTotalEnCash = dbHelper.getTotalCashAmountOfInvoiceInThatDay(startTime, endTime, startDate, endDate);
            montantTotalCash = montantTotalEnCash + montantTotalEnCashClient;

            mTotalCreditTextView.setText(Config.formaterSolde(montantTotalACredit));
            mTotalCashTextView.setText(Config.formaterSolde(montantTotalCash));

            itemMaxQty = dbHelper.getMaxOfItemsQuantity(startTime, endTime, startDate, endDate); // Recupere le maximun des quantites

            SalesByDayRefAdapter mDayRefAdapterTime = new SalesByDayRefAdapter(this, productList, itemMaxQty);
            ListView listViewByTime = findViewById(R.id.lv_ref);
            listViewByTime.setAdapter(mDayRefAdapterTime);
        }
    }



    public void clearTimeUI() {
        tvHourAndMinute.setText("");
        tvHourAndMinuteEnd.setText("");
        tvAmount.setText("");
        mTotalCashTextView.setText("");
        mTotalCreditTextView.setText("");
    }

    /**
     * @author birante sy
     * birantesy@gmail.com
     * Ferme automatiquement le clavier android.
     */
    public void closeDelfaultKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}