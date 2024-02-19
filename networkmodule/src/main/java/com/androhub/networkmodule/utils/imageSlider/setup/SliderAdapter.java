package com.androhub.networkmodule.utils.imageSlider.setup;

import static com.androhub.networkmodule.utils.Utils.isArLang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.androhub.networkmodule.R;
import com.androhub.networkmodule.local.Utility;
import com.androhub.networkmodule.mvvm.model.response.TicketBean;
import com.androhub.networkmodule.utils.Constant;
import com.androhub.networkmodule.utils.ImageDisplayUitls;
import com.androhub.networkmodule.utils.Utils;
import com.androhub.networkmodule.utils.imageSlider.SliderViewAdapter;
import com.mackhartley.roundedprogressbar.RoundedProgressBar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private View.OnClickListener onClickListener;

    private ArrayList<TicketBean> ticketResponses;


    public SliderAdapter(Context context, ArrayList<TicketBean> ticketResponses, View.OnClickListener onClickListener) {
        this.context = context;
        this.ticketResponses = ticketResponses;
        this.onClickListener = onClickListener;
    }

    public void setListItems(ArrayList<TicketBean> mList) {
        this.ticketResponses = mList;

    }

    public void renewItems(ArrayList<TicketBean> mList) {
        this.ticketResponses = mList;
        notifyDataSetChanged();

    }

    public TicketBean getItem(int pos) {
        return ticketResponses.get(pos);
    }
  /*  fun getItem(pos: Int): SearchDirectoryListResponseBean.Data.Hit {
        return list.get(pos)!!
    }*/
//
//    public void deleteItem(int position) {
//        this.mSliderItems.remove(position);
//        notifyDataSetChanged();
//    }
//
//    public void addItem(SliderItemModel sliderItem) {
//        this.mSliderItems.add(sliderItem);
//        notifyDataSetChanged();
//    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, parent, false);
        return new SliderAdapterVH(inflate);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        TicketBean bean = ticketResponses.get(position);

        viewHolder.llMAinSliderAdapter.setVisibility(View.GONE);
        viewHolder.llEservice.setVisibility(View.GONE);
        viewHolder.ll_appointment_new.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(bean.getTicketType()) && bean.getTicketType().equalsIgnoreCase(Constant.TICKET_TYPE.ESERVICE)) {
            viewHolder.llEservice.setTag(position);
            viewHolder.llEservice.setOnClickListener(onClickListener);
            viewHolder.llEservice.setVisibility(View.VISIBLE);
            viewHolder.tvBankNameEservice.setText(bean.getMerchantName());
            Long serverCreatedDate = bean.getCreatedAt();
            viewHolder.tvDayEservice.setText(Utils.getDateEserviceTwo(serverCreatedDate));
            viewHolder.tvTicketNumberRservice.setText(bean.getNumber());


            viewHolder.tvServiceTitle.setText(bean.getServiceName());
            ImageDisplayUitls.displayServiceImage(bean.getMerchantLogo(), context, viewHolder.ivLogoEservice);
            if (Objects.requireNonNull(bean.getStatus()).isEmpty()|| bean.getStatus()==null){
                viewHolder.tvdescService.setText(context.getResources().getString(R.string.submitted));
                viewHolder.esProgressbar.setProgressPercentage(10,false);
            }else if(bean.getStatus().equals(Constant.TICKET_STATUS.STATUS_PROCESSED)){
                viewHolder.tvdescService.setText(context.getResources().getString(R.string.processiong_application));
                viewHolder.esProgressbar.setProgressPercentage(50,false);
            }else {
                viewHolder.esProgressbar.setProgressPercentage(100,false);
            }

        }
        else if (!TextUtils.isEmpty(bean.getTicketType()) && bean.getTicketType().equalsIgnoreCase(Constant.TICKET_TYPE.DONATION)) {
            viewHolder.llEservice.setTag(position);
            viewHolder.llEservice.setOnClickListener(onClickListener);
            viewHolder.llEservice.setVisibility(View.VISIBLE);
            viewHolder.tvBankNameEservice.setText(bean.getMerchantName());
            Long serverCreatedDate = bean.getCreatedAt();
            viewHolder.tvDayEservice.setText(Utils.getDateEserviceTwo(serverCreatedDate));
            viewHolder.tvTicketNumberRservice.setText(bean.getNumber());


            viewHolder.tvServiceTitle.setText(bean.getServiceName());
            ImageDisplayUitls.displayServiceImage(bean.getMerchantLogo(), context, viewHolder.ivLogoEservice);
            if (Objects.requireNonNull(bean.getStatus()).isEmpty()|| bean.getStatus()==null){
                viewHolder.tvdescService.setText(context.getResources().getString(R.string.submitted));
                viewHolder.esProgressbar.setProgressPercentage(10,false);
            }else if(bean.getStatus().equals(Constant.TICKET_STATUS.STATUS_PROCESSED)){
                viewHolder.tvdescService.setText(context.getResources().getString(R.string.processiong_application));
                viewHolder.esProgressbar.setProgressPercentage(50,false);
            }else {
                viewHolder.esProgressbar.setProgressPercentage(100,false);
            }

        }
        else if (!TextUtils.isEmpty(bean.getTicketType()) && bean.getTicketType().equalsIgnoreCase(Constant.TICKET_TYPE.TI_APPOINTMENT))    {


            viewHolder.llMAinSliderAdapter.setVisibility(View.GONE);
            viewHolder.ll_appointment_new.setVisibility(View.VISIBLE);
            viewHolder.tvTicketNumberTwo.setText(bean.getNumber());
            viewHolder.tvBankNameTwo.setText(bean.getMerchantName());
            viewHolder.tvBranchNameTwo.setText(bean.getBranchName());
            viewHolder.tvAppointmentTitleTwo.setText(context.getResources().getString(R.string.your_appointment_withdrawal_service, bean.getServiceName()));

            ImageDisplayUitls.displayImageSlider(bean.getMerchantLogo(), context, viewHolder.ivLogoTwo, bean.getMerchantName());
            if (!TextUtils.isEmpty(bean.getAppointmentDate())) {

                String ticketDate = Utils.parseDate(bean.getAppointmentDate(), Constant.APPOINTMENT_DATE_FORMAT2, Constant.DISPLAY_DATE_FORMAT_TICKET);
                viewHolder.tvDayAppomitmentTwo.setText(ticketDate);//Monday, October 26th


                String startTime = getFormatTimeWithTZ(bean.getAppointmentTime());
                String endTime = getFormatTimeWithTZ(bean.getAppointmentEndTime());
//                    String startTime = getDate(Long.parseLong(Objects.requireNonNull(bean.getAppointmentTime())),"HH:MM");
//                    String endTime = getDate(Long.parseLong(Objects.requireNonNull(bean.getAppointmentEndTime())),"HH:MM");
                viewHolder.tvTimeAppointmentTwo.setText(startTime + "-" + endTime);//13:15-13:30
//                    viewHolder.tvMonthNameTwo.setText(Utils.getMonth3LettersName(date).toUpperCase());//OCT
//                    viewHolder.tvMonthDateTwo.setText(monthDate);//26
            }
            viewHolder.ll_appointment_new.setTag(position);
            viewHolder.llMAinSliderAdapter.setTag(position);
            viewHolder.ll_appointment_new.setOnClickListener(onClickListener);
            viewHolder.llMAinSliderAdapter.setOnClickListener(onClickListener);
//                viewHolder.tvCheckIn.setOnClickListener(onClickListener);
            if (!TextUtils.isEmpty(bean.getStatus()) && bean.getStatus().equalsIgnoreCase(Constant.TICKET_STATUS.STATUS_ON_HOLD)) {
                String status = Utils.getTicketStatus(context, bean.getStatus());
                viewHolder.tvGotoCounterTwo.setVisibility(View.GONE);
                viewHolder.tvStatusTwo.setVisibility(View.VISIBLE);
                viewHolder.llStatusTwo.setVisibility(View.VISIBLE);
                viewHolder.tvStatusTwo.setText(status);
                viewHolder.llAppGotoCounter.setVisibility(View.GONE);
                viewHolder.tvMissedTwo.setVisibility(View.GONE);

            }
            else if (!TextUtils.isEmpty(bean.getStatus()) && bean.getStatus().equalsIgnoreCase(Constant.TICKET_STATUS.STATUS_PROCESSED)) {
                viewHolder.llAppGotoCounter.setVisibility(View.VISIBLE);
                viewHolder.llTicket.setVisibility(View.GONE);
                viewHolder.tvStatusTwo.setVisibility(View.GONE);
                viewHolder.tvMissedTwo.setVisibility(View.GONE);
                viewHolder.llStatusTwo.setVisibility(View.GONE);
                viewHolder.tvGotoCounterTwo.setVisibility(View.VISIBLE);
                if (isArLang()) {
                    viewHolder.tvGotoCounterTwo.setText(Utility.fromHtml(context.getResources().getString(R.string.go_to_counter_str, bean.getCounter())));

                    viewHolder.tvLocationTwo.setText(bean.getLocationArabic());
                    viewHolder.tvLocation.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tvLocation.setText(bean.getLocation());

                    viewHolder.tvGotoCounterTwo.setText(Utility.fromHtml(context.getResources().getString(R.string.go_to_counter_str, bean.getCounter())));
//                    viewHolder.tvGotoCounterTwo.setText(Utility.fromHtml(context.getResources().getString(R.string.go_to_counter_str, bean.getCounter())));
                    viewHolder.tvLocationTwo.setText(bean.getLocation());
                    viewHolder.tvLocation.setVisibility(View.VISIBLE);
                }

            }
            else if (!TextUtils.isEmpty(bean.getStatus()) && bean.getStatus().equalsIgnoreCase(Constant.TICKET_STATUS.STATUS_NO_SHOW)) {
                if (bean.getNoShow()) {
                    DateTime currentTime = new DateTime();
                    viewHolder.llAppGotoCounter.setVisibility(View.GONE);
                    viewHolder.tvGotoCounterTwo.setVisibility(View.GONE);
                    viewHolder.tvMissedTwo.setVisibility(View.VISIBLE);
                    viewHolder.tvStatusTwo.setVisibility(View.GONE);
                    viewHolder.llStatusTwo.setVisibility(View.GONE);
                    DateTime someDate = new DateTime(Long.valueOf(bean.getNoShowExpireTime()), DateTimeZone.getDefault());


                    if (currentTime.isAfter(someDate)) {
                        if (isArLang()) {
                            viewHolder.tvMissedTwo.setText(context.getResources().getString(R.string.status_no_show));

                        } else {
                            viewHolder.tvMissedTwo.setText(context.getResources().getString(R.string.status_no_show));

                        }
                    } else {
                        Duration duration = new Duration(currentTime, someDate.plusMinutes(1));

                        // Log.e("ttimes", duration.getStandardMinutes() + "<<< duration");


                        if (isArLang()) {
                            viewHolder.tvMissedTwo.setText(Utility.fromHtml(context.getResources().getString(R.string.missed_your_turn, Utility.convertToArabic(String.valueOf(duration.getStandardMinutes())))));
                            viewHolder.tvLocation.setVisibility(View.GONE);
                        } else {
                            viewHolder.tvMissedTwo.setText(Utility.fromHtml(context.getResources().getString(R.string.missed_your_turn, duration.getStandardMinutes())));
                        }
                    }
//                    viewHolder.tvGotoCounterTwo.setVisibility(View.VISIBLE);
                    viewHolder.llTicket.setVisibility(View.GONE);
                    viewHolder.llStatus.setVisibility(View.GONE);
                }


            }
            else {
                viewHolder.tvMissedTwo.setVisibility(View.GONE);
                viewHolder.tvStatusTwo.setVisibility(View.GONE);
                viewHolder.llStatusTwo.setVisibility(View.GONE);
                viewHolder.llStatus.setVisibility(View.GONE);
                viewHolder.tvMissedTwo.setVisibility(View.GONE);
                viewHolder.llAppGotoCounter.setVisibility(View.GONE);
                viewHolder.tvGotoCounterTwo.setVisibility(View.GONE);
//                    viewHolder.tvGotoCounterNumber.setVisibility(View.GONE);
                viewHolder.tvLocation.setVisibility(View.INVISIBLE);
                viewHolder.llTicket.setVisibility(View.VISIBLE);


            }
        }
        else {

            viewHolder.llMAinSliderAdapter.setVisibility(View.VISIBLE);
            viewHolder.ll_appointment_new.setVisibility(View.GONE);
            viewHolder.tvMissed.setVisibility(View.GONE);
            viewHolder.llStatus.setVisibility(View.GONE);
            viewHolder.llInstantCounter.setVisibility(View.GONE);
            viewHolder.tvTicketNumber.setText(bean.getNumber());

            viewHolder.tvBranchName.setText(bean.getBranchName());

            if (bean.getTicketType() != null) {
//                if (bean.getTicketType().equals(Constant.NEW_EVENT.EVENT)) {
//                    viewHolder.tvMember.setVisibility(View.GONE);
//                    ImageDisplayUitls.displayImageSlider(bean.getEventLogo(), context, viewHolder.ivLogo, bean.getEventName());
//                    viewHolder.tvBankName.setText(bean.getEventName());
//
//
//                } else {
                    viewHolder.tvMember.setVisibility(View.VISIBLE);
                    ImageDisplayUitls.displayImageSlider(bean.getMerchantLogo(), context, viewHolder.ivLogo, bean.getMerchantName());
                    viewHolder.tvBankName.setText(bean.getMerchantName());
//                }
            } else {
                ImageDisplayUitls.displayImageSlider(bean.getMerchantLogo(), context, viewHolder.ivLogo, bean.getMerchantName());
                viewHolder.tvBankName.setText(bean.getMerchantName());
            }
            if (bean.getCustomersCount() >= 0)
                viewHolder.tvMember.setText(Utils.showMemberToDisplay(context, "" + bean.getCustomersCount()));
            Double time = bean.getWaitTime();


            try {

                if (bean.getTicketType() != null) {
                    if (bean.getTicketType().equals("event")) {
                        String dateStr = bean.getStarttime();
                        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                        df2.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date date = df2.parse(dateStr);
                        df2.setTimeZone(TimeZone.getDefault());
                        String formattedDate = df2.format(date);

                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                        Date startDate = new Date();
                        String startDateCurrent = inputFormat.format(startDate);

                        Date startTime = inputFormat.parse(formattedDate);
                        Date currentTime = inputFormat.parse(startDateCurrent);


                        long difference = startTime.getTime() - currentTime.getTime();
                        long days = (difference / (1000 * 60 * 60 * 24));
//            long hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60));
                        long min =
                                (difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60);
                        if (min == 0 || min < 0) {

                            if (min == 0) {
                                viewHolder.tvTime.setText(context.getResources().getString(R.string.less_then_miniut));
                            } else {
                                String endDateStr = bean.getEndTime();
                                df2.setTimeZone(TimeZone.getTimeZone("UTC"));
                                Date ednDate = df2.parse(endDateStr);
                                df2.setTimeZone(TimeZone.getDefault());
                                String EndDateFormattedDate = df2.format(ednDate);
                                Date endTime = inputFormat.parse(EndDateFormattedDate);

                                long difference2 = endTime.getTime() - currentTime.getTime();
                                long days2 = (difference2 / (1000 * 60 * 60 * 24));
                                long min2 =
                                        (difference2 - 1000 * 60 * 60 * 24 * days2) / (1000 * 60);


                                if (min2 == 0) {
                                    viewHolder.tvTime.setText(context.getResources().getString(R.string.less_then_miniut));
                                } else {
                                    viewHolder.tvTime.setText(Utils.showTimeToDisplay(context, Double.valueOf(min2)));
                                }

                            }

                        } else {
                            viewHolder.tvTime.setText(Utils.showTimeToDisplay(context, Double.valueOf(min)));
                        }

                    }
                    else {
                        viewHolder.tvTime.setText(Utils.showTimeToDisplay(context, time));
                    }
                } else {
                    viewHolder.tvTime.setText(Utils.showTimeToDisplay(context, time));
                }


            } catch (Exception e) {

            }


            Long serverCreatedDate = bean.getCreatedAt();
            viewHolder.tvDate.setText(Utils.getDate(serverCreatedDate));
            viewHolder.tvDate.setVisibility(View.VISIBLE);
            viewHolder.tvAppointmentTitle.setText(context.getResources().getString(R.string.your_ticket_withdrawal_service, bean.getServiceName()));


            // @Yuvrasjinsh
//            if (bean.getTicketType() != null) {
////                if (bean.getTicketType().equals(Constant.NEW_EVENT.EVENT)) {
////                    viewHolder.tvAppointmentTitle.setText(
////                            String.format(context.getResources().getString(R.string.your_ticket_withdrawal_service_new), bean.getServiceName(), bean.getRouteName())
////                    );
////                }
//            }


            if (!TextUtils.isEmpty(bean.getStatus()) && bean.getStatus().equalsIgnoreCase(Constant.TICKET_STATUS.STATUS_ON_HOLD)) {
                String status = Utils.getTicketStatus(context, bean.getStatus());

                viewHolder.llStatus.setVisibility(View.VISIBLE);

                viewHolder.tvStatus.setText(status);
                viewHolder.llTicket.setVisibility(View.GONE);
                viewHolder.llInstantCounter.setVisibility(View.GONE);

//                    viewHolder.tvGotoCounterNumber.setVisibility(View.GONE);

            } else if (!TextUtils.isEmpty(bean.getStatus()) && bean.getStatus().equalsIgnoreCase(Constant.TICKET_STATUS.STATUS_PROCESSED))/*||bean.getWaitTime()<=0 && bean.getCustomersCount()<=0 && !TextUtils.isEmpty(bean.getCounter()))*/ {
                viewHolder.llTicket.setVisibility(View.GONE);
                viewHolder.llStatus.setVisibility(View.GONE);
                viewHolder.llInstantCounter.setVisibility(View.VISIBLE);
                viewHolder.tvGotoCounter.setVisibility(View.VISIBLE);
                if (isArLang()) {
                    viewHolder.tvGotoCounter.setText(Utility.fromHtml(context.getResources().getString(R.string.go_to_counter_str, bean.getCounter())));
                    viewHolder.tvLocation.setText(bean.getLocationArabic());

//                        viewHolder.tvGotoCounterNumber.setText(Utility.convertToArabic(String.valueOf(bean.getCounter())));
//                        viewHolder.tvGotoCounterNumber.setVisibility(View.VISIBLE);
                    viewHolder.tvLocation.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tvLocation.setText(bean.getLocation());
                    viewHolder.tvGotoCounter.setText(Utility.fromHtml(context.getResources().getString(R.string.go_to_counter_str, bean.getCounter())));
                    viewHolder.tvLocation.setVisibility(View.VISIBLE);
                }

            } else if (!TextUtils.isEmpty(bean.getStatus()) && bean.getStatus().equalsIgnoreCase(Constant.TICKET_STATUS.STATUS_NO_SHOW)) {
                if (bean.getNoShow()) {
                    DateTime currentTime = new DateTime();


                    DateTime someDate = new DateTime(Long.valueOf(bean.getNoShowExpireTime()), DateTimeZone.getDefault());


//                    if (currentTime.isAfter(someDate)) {
//
//                        if (isArLang()) {
//                            viewHolder.tvGotoCounter.setText(context.getResources().getString(R.string.status_no_show));
////                                viewHolder.tvGotoCounterNumber.setVisibility(View.GONE);
//                            viewHolder.tvLocation.setVisibility(View.INVISIBLE);
//                        } else {
//                            viewHolder.tvGotoCounter.setText(context.getResources().getString(R.string.status_no_show));
////                                viewHolder.tvGotoCounterNumber.setVisibility(View.GONE);
//                            viewHolder.tvLocation.setVisibility(View.INVISIBLE);
//                        }
//                    } else {
                        Duration duration = new Duration(currentTime, someDate.plusMinutes(1));

                            viewHolder.tvMissed.setVisibility(View.VISIBLE);
                            if (isArLang()) {

                                viewHolder.tvMissed.setText(Utility.fromHtml(context.getResources().getString(R.string.missed_your_turn, Utility.convertToArabic(String.valueOf(duration.getStandardMinutes())))));
//                                viewHolder.tvGotoCounterNumber.setVisibility(View.GONE);
                                viewHolder.tvLocation.setVisibility(View.GONE);

                                viewHolder.ivForwardArrow.setVisibility(View.GONE);
                            } else {
                                viewHolder.tvMissed.setText(Utility.fromHtml(context.getResources().getString(R.string.missed_your_turn, duration.getStandardMinutes())));
//                                viewHolder.tvGotoCounterNumber.setVisibility(View.GONE);
                                viewHolder.tvLocation.setVisibility(View.GONE);
                                viewHolder.ivForwardArrow.setVisibility(View.GONE);
                            }
//                        }


//                    }

                    viewHolder.llAppGotoCounter.setVisibility(View.GONE);
                    viewHolder.tvGotoCounter.setVisibility(View.GONE);
                    viewHolder.llTicket.setVisibility(View.GONE);
                    viewHolder.llStatus.setVisibility(View.GONE);
                }


            }
            else {
                viewHolder.tvGotoCounter.setVisibility(View.GONE);
//                    viewHolder.tvGotoCounterNumber.setVisibility(View.GONE);
                viewHolder.tvLocation.setVisibility(View.INVISIBLE);
                viewHolder.llTicket.setVisibility(View.VISIBLE);

                viewHolder.llStatus.setVisibility(View.GONE);

            }
            if (bean.getAgentCount() == 0) {
                viewHolder.tvTime.setText(context.getResources().getString(R.string.awaiting_processing));
            } else {
                String[] list = bean.getBranchOpenTime().split(",");
                Double time3 = bean.getWaitTime();
                for (int i = 0; i < list.length; i++) {
//            Log.e("valueofI", i + "<<<<---");
                    try {
                        LocalTime time2 = new LocalTime();
                        if (time2.isAfter(LocalTime.parse(list[i])) && time2.isBefore(LocalTime.parse(list[i + 1]))) {
                            viewHolder.tvTime.setText(Utils.showTimeToDisplay(context, time3));
                            // Log.e("Timessawabtween", "22");
                            break;
                        } else {
                            viewHolder.tvTime.setText(context.getResources().getString(R.string.awaiting_processing));
                            // Log.e("Timessawatingggg", "111");

                        }
                        i++;

                    } catch (Exception e) {
                        // Log.e("Timessawatingggg", e.getMessage() + "000<<");
                        e.printStackTrace();
                    }
                }
            }

            viewHolder.ll_appointment_new.setTag(position);
            viewHolder.llMAinSliderAdapter.setTag(position);
//                viewHolder.tvCheckIn.setTag(position);
            viewHolder.ll_appointment_new.setOnClickListener(onClickListener);
            viewHolder.llMAinSliderAdapter.setOnClickListener(onClickListener);
//                viewHolder.tvCheckIn.setOnClickListener(onClickListener);
        }


    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return ticketResponses.size();
    }

    //    public static String getDate(long milliSeconds, String dateFormat)
//    {
//        // Create a DateFormatter object for displaying date in specified format.
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//
//        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(milliSeconds);
//        return formatter.format(calendar.getTime());
//    }
    public static String getFormatTimeWithTZ(String mills) {
        Date date11 = new Date(Long.parseLong(Objects.requireNonNull(mills)));
        SimpleDateFormat timeZoneDate = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeZoneDate.format(date11);
    }

    static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;

        LinearLayoutCompat llAppGotoCounter;
        LinearLayoutCompat llStatus;
        LinearLayoutCompat llInstantCounter;
        LinearLayout llMAinSliderAdapter;
        LinearLayout llEservice;
        RoundedProgressBar esProgressbar;
        LinearLayout ll_appointment_new;
        AppCompatImageView ivLogo;
        AppCompatImageView ivLogoEservice;
        AppCompatImageView ivForwardArrow;
        LinearLayoutCompat llStatusTwo;
        AppCompatImageView ivLogoTwo;
        TextView tvDate;
        TextView tvLocation;
        TextView tvDayAppomitment;
        TextView tvTimeAppointment;
        TextView tvMonthName;
        TextView tvMonthDate;
        TextView tvTicketNumber;
        TextView tvTicketNumberTwo;
        TextView tvDayAppomitmentTwo;
        TextView tvTimeAppointmentTwo;
        TextView tvServiceTitle;
        TextView tvBankNameEservice;
        TextView tvDayEservice;
        TextView tvTicketNumberRservice;
        TextView tvdescService;
        //        TextView tvMonthNameTwo;
//        TextView tvMonthDateTwo;
        TextView tvBranchNameTwo;
        //        TextView tvCheckIn;
//        TextView tvCheckedIn;
        TextView tvBankName;
        TextView tvBankNameTwo;
        TextView tvBranchName;
        TextView tvAppointmentTitle;
        TextView tvAppointmentTitleTwo;
        TextView tvMember;
        TextView tvTime;
        TextView tvGotoCounter;
        TextView tvMissed;

        TextView tvMissedTwo;
        TextView tvGotoCounterTwo;
        TextView tvLocationTwo;
        //        TextView tvGotoCounterNumber;
//        CardView cvAppontment;
        LinearLayout llTicket;
        TextView tvStatus;
        TextView tvStatusTwo;
//        LinearLayout llAppointmentDate;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            tvBranchNameTwo = itemView.findViewById(R.id.tvBranchNameTwo);
//            tvMonthDateTwo = itemView.findViewById(R.id.tvMonthDateTwo);
//            tvMonthNameTwo = itemView.findViewById(R.id.tvMonthNameTwo);
            tvTimeAppointmentTwo = itemView.findViewById(R.id.tvTimeAppointmentTwo);
            tvServiceTitle = itemView.findViewById(R.id.tvServiceTitle);
            tvBankNameEservice = itemView.findViewById(R.id.tvBankNameEservice);
            tvDayEservice = itemView.findViewById(R.id.tvDayEservice);
            tvTicketNumberRservice = itemView.findViewById(R.id.tvTicketNumberRservice);
            tvdescService = itemView.findViewById(R.id.tvdescService);
            tvDayAppomitmentTwo = itemView.findViewById(R.id.tvDayAppomitmentTwo);
            ll_appointment_new = itemView.findViewById(R.id.ll_appointment_new);
            tvTicketNumberTwo = itemView.findViewById(R.id.tvTicketNumberTwo);
            tvLocationTwo = itemView.findViewById(R.id.tvLocationTwo);
            tvBankNameTwo = itemView.findViewById(R.id.tvBankNameTwo);
            llMAinSliderAdapter = itemView.findViewById(R.id.llMAinSliderAdapter);
            llEservice = itemView.findViewById(R.id.llEservice);
            esProgressbar = itemView.findViewById(R.id.esProgressbar);
//            tvCheckedIn = itemView.findViewById(R.id.tvCheckedIn);
//            tvCheckIn = itemView.findViewById(R.id.tvCheckIn);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            ivLogoEservice = itemView.findViewById(R.id.ivLogoEservice);
            ivLogoTwo = itemView.findViewById(R.id.ivLogoTwo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLocation = itemView.findViewById(R.id.tvLocation);

            tvTicketNumber = itemView.findViewById(R.id.tvTicketNumber);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            tvBranchName = itemView.findViewById(R.id.tvBranchName);
//            cvAppontment = itemView.findViewById(R.id.cvAppontment);
            tvAppointmentTitle = itemView.findViewById(R.id.tvAppointmentTitle);
            tvAppointmentTitleTwo = itemView.findViewById(R.id.tvAppointmentTitleTwo);
//            llAppointmentDate = itemView.findViewById(R.id.llAppointmentDate);
            llTicket = itemView.findViewById(R.id.llTicket);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            llStatus = itemView.findViewById(R.id.llStatus);
            tvStatusTwo = itemView.findViewById(R.id.tvStatusTwo);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvGotoCounter = itemView.findViewById(R.id.tvGotoCounter);
            tvMissed = itemView.findViewById(R.id.tvMissed);
            tvMissedTwo = itemView.findViewById(R.id.tvMissedTwo);
            tvGotoCounterTwo = itemView.findViewById(R.id.tvGotoCounterTwo);
//            tvGotoCounterNumber = itemView.findViewById(R.id.tvGotoCounterNumber);
            tvMember = itemView.findViewById(R.id.tvMember);
            llAppGotoCounter = itemView.findViewById(R.id.llAppGotoCounter);
            llInstantCounter = itemView.findViewById(R.id.llInstantCounter);
            llStatusTwo = itemView.findViewById(R.id.llStatusTwo);
            ivForwardArrow = itemView.findViewById(R.id.ivForwardArrow);
            this.itemView = itemView;
        }
    }

}
