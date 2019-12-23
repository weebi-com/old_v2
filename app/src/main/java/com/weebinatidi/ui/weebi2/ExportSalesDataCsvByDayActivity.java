package com.weebinatidi.ui.weebi2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.weebinatidi.R;
import com.weebinatidi.model.DbHelper;
import com.weebinatidi.ui.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import static com.weebinatidi.export.ExportData.exportAllCustomersInCsv;
import static com.weebinatidi.export.ExportData.exportAllInvoicesInCsv;
import static com.weebinatidi.export.ExportData.exportAllItemsSoldInCsv;
import static com.weebinatidi.export.ExportData.exportAllItemsSoldPerPeriodInCsv;
import static com.weebinatidi.export.ExportData.exportAllProductInCsv;
import static com.weebinatidi.utils.FileUtils.createExportDirectoryIfNotExist;
import static com.weebinatidi.utils.FileUtils.racinePlusExportCSVDirectory;

public class ExportSalesDataCsvByDayActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    DbHelper dbHelper;

    Button exportItemSoldByPeriodBtn;
    TextView tvDay;
    TextView tvDayEnd;

    String startDate;
    String endDate;

    Button exportAllCustomerBtn;
    Button exportAllProductBtn;
    Button exportAllInvoiceBtn;
    Button exportAllItemSoldBtn;

    Button btnSave;


    boolean isExported;

    public void initView() {

        tvDay = findViewById(R.id.tv_day);
        tvDayEnd = findViewById(R.id.tv_day_end);

        exportItemSoldByPeriodBtn = findViewById(R.id.btn_export_item_sold_by_period);
        exportAllCustomerBtn = findViewById(R.id.btn_export_customers);
        exportAllProductBtn = findViewById(R.id.btn_export_product);
        exportAllInvoiceBtn = findViewById(R.id.btn_export_invoice);
        exportAllItemSoldBtn = findViewById(R.id.btn_export_item_sold);
        btnSave = findViewById(R.id.btn_save);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        closeDelfaultKeyboard();
        initView();

        createExportDirectoryIfNotExist();

        exportItemSoldByPeriodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        ExportSalesDataCsvByDayActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        exportAllCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExported = exportAllCustomersInCsv(ExportSalesDataCsvByDayActivity.this);
                showMessage(isExported);
            }
        });


        exportAllProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExported = exportAllProductInCsv(ExportSalesDataCsvByDayActivity.this);
                showMessage(isExported);
            }
        });


        exportAllInvoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExported = exportAllInvoicesInCsv(ExportSalesDataCsvByDayActivity.this);
                showMessage(isExported);
            }
        });


        exportAllItemSoldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExported = exportAllItemsSoldInCsv(ExportSalesDataCsvByDayActivity.this);
                showMessage(isExported);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareExportedFiles();
                Toast.makeText(getApplicationContext(), "Exporter", Toast.LENGTH_LONG).show();
            }
        });
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

        isExported = exportAllItemsSoldPerPeriodInCsv(this, startDate, endDate);
        showMessage(isExported);

    }

    public void closeDelfaultKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_export_sales_data_csv_by_day;
    }

    private void shareExportedFiles() {

        Intent shareIntent = new Intent();
        ArrayList<Uri> arrayUri = new ArrayList<>();

        File fileList = new File(racinePlusExportCSVDirectory);
        File[] filenames = fileList.listFiles();
        for (File file : filenames) {
            arrayUri.add(Uri.fromFile(file));
        }

        if (arrayUri.isEmpty()) {
        } else if (arrayUri.size() == 1) {
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, arrayUri.get(0));
        } else {
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayUri);
        }

        shareIntent.setType("text/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Envoyer"));
    }


    void showMessage(boolean info) {
        if (info) {
            Toast.makeText(this, "Exporter avec success ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Non Exporter ", Toast.LENGTH_LONG).show();
        }
    }
}

