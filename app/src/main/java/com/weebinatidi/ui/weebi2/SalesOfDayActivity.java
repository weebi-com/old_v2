package com.weebinatidi.ui.weebi2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.weebinatidi.Config;
import com.weebinatidi.R;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.ui.BaseActivity;

import java.util.Calendar;
import java.util.Date;

import static com.weebinatidi.Config.getFormattedDate;



/***
 *
 *
 * Cette classe permet
 * d'avoir le total Total des :
 * - Ventes Cash par Jour
 * - Ventes a Credit au Client Fidele / Jour
 * - Ventes Oublier / Jour
 *
 * @author birantesy
 * birantesy@gmail.com
 * Working Well...
 *
 */
public class SalesOfDayActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {


    DbHelper dbHelper;
    AlertDialog.Builder registerSalesBuilder;
    private TextView tVTotalAmount;
    private TextView tVTotalAmountOfCredit;
    private TextView tVTotalAmountOfCash;
    private TextView tVForgetSalesValue;
    private ImageView imageView;


    TextView tvDay;
    TextView tvDayEnd;
    Button dateButton;


    String startDate;
    String endDate;


    public void initView(){
        tvDay = findViewById(R.id.tv_day);
        tvDayEnd = findViewById(R.id.tv_day_end);

        dateButton = findViewById(R.id.btn_date);

        tVTotalAmount = findViewById(R.id.tv_sales_value);
        tVTotalAmountOfCredit = findViewById(R.id.tv_sales_by_credit_value);
        tVTotalAmountOfCash = findViewById(R.id.tv_sales_by_cash_value);
        tVForgetSalesValue = findViewById(R.id.tv_forget_sales_value);

        imageView = findViewById(R.id.iv_update);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DbHelper(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        initView();

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        SalesOfDayActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addForgetSales();
            }
        });
        updateUI();
    }


    /**
     * @author birante sy
     * birantesy@gmail.com
     * Enregistre une vente oublier.
     */
    private void addForgetSales() {

        registerSalesBuilder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.dialog_add_forget_sales, null, false);

        final EditText forgetSalesAmount = customView.findViewById(R.id.et_forget_sales_amount);
        final EditText confirmForgetSalesAmount = customView.findViewById(R.id.et_confirm_forget_sales_amount);
        final ImageView okbtn = customView.findViewById(R.id.btn_save);

        final LinearLayout lettre=customView.findViewById(R.id.clavl);
        final LinearLayout numeriq=customView.findViewById(R.id.clavn);

        final Button espaced,cleard,cleardn,un, deux, trois, quatre, cinq, six, sept, huit, neuf, zero;

        espaced=(Button)customView.findViewById(R.id.espaced);
        cleard=(Button)customView.findViewById(R.id.btnClearAl);

        un=(Button)customView.findViewById(R.id.btnNum1Id);
        deux=(Button)customView.findViewById(R.id.btnNum2Id);
        trois=(Button)customView.findViewById(R.id.btnNum3Id);
        quatre=(Button)customView.findViewById(R.id.btnNum4Id);
        cinq=(Button)customView.findViewById(R.id.btnNum5Id);
        six=(Button)customView.findViewById(R.id.btnNum6Id);
        sept=(Button)customView.findViewById(R.id.btnNum7Id);
        huit=(Button)customView.findViewById(R.id.btnNum8Id);
        neuf=(Button)customView.findViewById(R.id.btnNum9Id);
        zero=(Button)customView.findViewById(R.id.btnNum0Id);
        cleardn=(Button)customView.findViewById(R.id.btnClearId);

        forgetSalesAmount.setInputType(0);
        confirmForgetSalesAmount.setInputType(0);

        forgetSalesAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(View.GONE);
                numeriq.setVisibility(View.VISIBLE);

                zero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=forgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            forgetSalesAmount.append(""+0);
                        }
                        else {
                            forgetSalesAmount.append(""+0);
                        }
                    }
                });

                un.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=forgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            forgetSalesAmount.append(""+1);
                        }
                        else {
                            forgetSalesAmount.append(""+1);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                deux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=forgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            forgetSalesAmount.append(""+2);
                        }
                        else {
                            forgetSalesAmount.append(""+2);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                trois.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=forgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            forgetSalesAmount.append(""+3);
                        }
                        else {
                            forgetSalesAmount.append(""+3);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                quatre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=forgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            forgetSalesAmount.append(""+4);
                        }
                        else {
                            forgetSalesAmount.append(""+4);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                cinq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=forgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            forgetSalesAmount.append(""+5);
                        }
                        else {
                            forgetSalesAmount.append(""+5);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                six.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=forgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            forgetSalesAmount.append(""+6);
                        }
                        else {
                            forgetSalesAmount.append(""+6);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                sept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=forgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            forgetSalesAmount.append(""+7);
                        }
                        else {
                            forgetSalesAmount.append(""+7);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                huit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=forgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            forgetSalesAmount.append(""+8);
                        }
                        else {
                            forgetSalesAmount.append(""+8);
                        }


                    }
                });

                neuf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=forgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            forgetSalesAmount.append(""+9);
                        }
                        else {
                            forgetSalesAmount.append(""+9);
                        }

                    }
                });

                cleardn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=forgetSalesAmount.getText().toString();
                        if (chaine.equals("")){

                        }else {
                            chaine = chaine.substring(0, chaine.length()-1);
                            forgetSalesAmount.setText(""+chaine);
                        }

                    }
                });
            }
        });


        confirmForgetSalesAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lettre.setVisibility(View.GONE);
                numeriq.setVisibility(View.VISIBLE);

                zero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=confirmForgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            confirmForgetSalesAmount.append(""+0);
                        }
                        else {
                            confirmForgetSalesAmount.append(""+0);
                        }
                    }
                });

                un.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=confirmForgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            confirmForgetSalesAmount.append(""+1);
                        }
                        else {
                            confirmForgetSalesAmount.append(""+1);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                deux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=confirmForgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            confirmForgetSalesAmount.append(""+2);
                        }
                        else {
                            confirmForgetSalesAmount.append(""+2);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                trois.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=confirmForgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            confirmForgetSalesAmount.append(""+3);
                        }
                        else {
                            confirmForgetSalesAmount.append(""+3);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                quatre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=confirmForgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            confirmForgetSalesAmount.append(""+4);
                        }
                        else {
                            confirmForgetSalesAmount.append(""+4);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                cinq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=confirmForgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            confirmForgetSalesAmount.append(""+5);
                        }
                        else {
                            confirmForgetSalesAmount.append(""+5);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                six.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=confirmForgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            confirmForgetSalesAmount.append(""+6);
                        }
                        else {
                            confirmForgetSalesAmount.append(""+6);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                sept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=confirmForgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            confirmForgetSalesAmount.append(""+7);
                        }
                        else {
                            confirmForgetSalesAmount.append(""+7);
                        }

                        //Toast.makeText(getApplicationContext(),""+txtResult.getText().toString(),Toast.LENGTH_LONG).show();


                    }
                });

                huit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=confirmForgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            confirmForgetSalesAmount.append(""+8);
                        }
                        else {
                            confirmForgetSalesAmount.append(""+8);
                        }


                    }
                });

                neuf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String chaine=confirmForgetSalesAmount.getText().toString();

                        if(chaine.contains("")){
                            confirmForgetSalesAmount.append(""+9);
                        }
                        else {
                            confirmForgetSalesAmount.append(""+9);
                        }

                    }
                });

                cleardn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String chaine=confirmForgetSalesAmount.getText().toString();
                        if (chaine.equals("")){

                        }else {
                            chaine = chaine.substring(0, chaine.length()-1);
                            confirmForgetSalesAmount.setText(""+chaine);
                        }

                    }
                });
            }
        });

        registerSalesBuilder.setCancelable(true);
        registerSalesBuilder.setView(customView);
        registerSalesBuilder.setTitle("Vente Oublier");
        final AlertDialog alertDialog = registerSalesBuilder.show();

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetSalesAmount.setError(null);
                confirmForgetSalesAmount.setError(null);

                String amount = forgetSalesAmount.getText().toString();
                String confirmAmount = confirmForgetSalesAmount.getText().toString();

                if (!TextUtils.isEmpty(amount)) {
                    if (amount.equals(confirmAmount)) {
                        boolean isFound = dbHelper.checkIfWeHaveForgetSaleToday();
                        if (isFound){
                            String id = String.valueOf(dbHelper.getForgetSalesId());
                            dbHelper.updateLastForgetSale(id, confirmAmount, getFormattedDate(new Date()));
                            updateUI();
                            alertDialog.dismiss();
                        } else {
                            dbHelper.createVentesOublier(amount, getFormattedDate(new Date()));
                            updateUI();
                            alertDialog.dismiss();
                        }
                    } else {
                        forgetSalesAmount.setError("les deux montants ne corresspondent pas");
                    }
                }
            }
        });

    }


    /**
     * @author birante sy
     * birantesy@gmail.com
     * Met a jour l'interface utlisateur.
     */
    public void updateUI() {
        int totalCredit = dbHelper.getTodayTotalCreditAmount();
        tVTotalAmountOfCredit.setText(Config.formaterSolde(totalCredit));

        int totalCashClient = dbHelper.getTodayTotalCashClientAmount();
        int totalCash = dbHelper.getTodayTotalCashAmount();

        int totalAmountOfCash = totalCashClient + totalCash;
        tVTotalAmountOfCash.setText(Config.formaterSolde(totalAmountOfCash));

        int totalFs = dbHelper.getTodayTotalAmountOfForgetSales();
        tVForgetSalesValue.setText(Config.formaterSolde(totalFs));

        int totalSales = dbHelper.getTodayTotalAmountOfInvoice();

        int total = totalSales + totalFs;
        tVTotalAmount.setText(Config.formaterSolde(total));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

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

        int totalCreditAmount = dbHelper.getTotalCreditAmountOfThisPeriod(startDate, endDate);
        int totalCustomerCashAmount = dbHelper.getTotalCustomerCashAmountOfThisPeriod(startDate, endDate);
        int totalCashAmount = dbHelper.getTotalCashAmountOfThisPeriod(startDate, endDate);
        int totalCash = totalCustomerCashAmount + totalCashAmount;
        int total = totalCreditAmount + totalCustomerCashAmount + totalCashAmount;

        tVTotalAmount.setText(Config.formaterSolde(total));
        tVTotalAmountOfCredit.setText(Config.formaterSolde(totalCreditAmount));
        tVTotalAmountOfCash.setText(Config.formaterSolde(totalCash));

        tVForgetSalesValue.setText(Config.formaterSolde(0));

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_sales_of_day;
    }

}
