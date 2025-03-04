package com.example.hotrovieclam.Fragment.Child_Fragment.Your_Child_Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.hotrovieclam.Model.HieuUngThongBao;
import com.example.hotrovieclam.Model.Profile;
import com.example.hotrovieclam.Model.UserSessionManager;
import com.example.hotrovieclam.R;
import com.example.hotrovieclam.databinding.FragmentThongTinCaNhanBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ThongTinCaNhanFragment extends Fragment {
    private FusedLocationProviderClient fusedLocationClient;
    private FragmentThongTinCaNhanBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    UserSessionManager userSessionManager = new UserSessionManager();
    String uid = userSessionManager.getUserUid();
    static String dc;
    Bundle bundle;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentThongTinCaNhanBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Khởi tạo FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Nhận UID từ bundle

        Log.d("TTT", "onCreateView: " + dc);
        //binding.editTextDC.setText("");
        //binding.editTextDC.setText(dc);
        // Hiển thị thông tin
        HienThiThongTin(uid);

        // Xử lý sự kiện nhấn nút back
        binding.back.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_buttom);
                if (bottomNav != null) {
                    bottomNav.setVisibility(View.GONE);
                }
            }
        });

        // Xử lý chọn ngày sinh
        binding.editTextDate.setOnClickListener(v -> showDatePickerDialog());

        // Xử lý cập nhật thông tin người dùng
        binding.btnUpdateInfo.setOnClickListener(v -> {
            binding.btnUpdateInfo.setVisibility(View.GONE);
            HieuUngThongBao.startLoadingAnimation(binding.loadding);
            binding.loadding.setVisibility(View.VISIBLE);
            Log.d("uid", "onClick: " + uid);
            String ngaysinh = binding.editTextDate.getText().toString().trim();
            String diachi = binding.editTextDC.getText().toString().trim();
            int gioitinh = 0;

            if (binding.rdNam.isChecked()) {
                gioitinh = 1;
            } else if (binding.rdNu.isChecked()) {
                gioitinh = 2;
            }

            // Kiểm tra dữ liệu
            if (ngaysinh.isEmpty() || diachi.isEmpty() || gioitinh == 0) {
                binding.loadding.setVisibility(View.GONE);
                HieuUngThongBao.showErrorToast(requireContext(),"Vui lòng nhập đầy đủ thông tin");
                //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                binding.btnUpdateInfo.setVisibility(View.VISIBLE);

                return;
            }

            Profile profile = new Profile(ngaysinh, diachi, gioitinh);
            addProfile(uid, profile);
        });

        // Sự kiện khi nhấn vào biểu tượng vị trí trong EditText
        binding.editTextDC.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.editTextDC.getRight() - binding.editTextDC.getCompoundDrawables()[2].getBounds().width())) {
                    //Toast.makeText(getContext(), "Bạn đã nhấn vào biểu tượng vị trí!", Toast.LENGTH_SHORT).show();
                    //getLastLocation();
                    Dialog_address address = new Dialog_address();
                    address.show(getParentFragmentManager(), "adress");
                    return true;
                }
            }
            return false;
        });
        bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("diachi") != null) {
                dc = bundle.getString("diachi");
                Log.d("TTT", "onCreateView: " + dc);
            }


        }
        return view;
    }

    // Hàm hiển thị DatePickerDialog
    public void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    binding.editTextDate.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    // Hàm hiển thị thông tin từ Firestore
    public void HienThiThongTin(String uid) {
        if (uid == null || uid.isEmpty()) {
            Log.e("HienThiThongTin", "UID không tồn tại hoặc rỗng");
            // Hiển thị thông báo lỗi cho người dùng hoặc thực hiện một hành động thay thế
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    binding.editTextEmail.setText(document.getString("email"));
                    binding.editTextsdt.setText(document.getString("phoneNumber"));

                    DocumentReference profileRef = db.collection("users").document(uid)
                            .collection("role").document("candidate")
                            .collection("profile").document(uid);
                    profileRef.get().addOnCompleteListener(profileTask -> {
                        if (profileTask.isSuccessful() && profileTask.getResult() != null) {
                            DocumentSnapshot profileDocument = profileTask.getResult();
                            if (profileDocument.exists()) {
                                binding.editTextDate.setText(profileDocument.getString("birthday"));
                                if (dc != null) {
                                    binding.editTextDC.setText(dc);
                                    Log.d("RRR", "HienThiThongTin: "+dc);
                                } else {
                                    binding.editTextDC.setText(profileDocument.getString("address"));
                                }
                                int gioitinh = profileDocument.getLong("gioitinh").intValue();
                                if (gioitinh == 1) binding.rdNam.setChecked(true);
                                else if (gioitinh == 2) binding.rdNu.setChecked(true);
                            }
                        }
                    });
                }
            }
        });
    }

    // Hàm cập nhật thông tin profile vào Firestore
    public void addProfile(String uid, Profile profile) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .collection("role").document("candidate")
                .collection("profile").document(uid)
                .set(profile)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Dữ liệu đã được lưu thành công với ID: " + uid);
                    HieuUngThongBao.showSuccessToast(requireContext(),"Cập nhật thành công");
                    //Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Bundle result = new Bundle();
                    result.putBoolean("update", true);
                    getParentFragmentManager().setFragmentResult("isupdate", result);
                    getParentFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Lỗi khi lưu dữ liệu", e);
                    Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                });
    }

    public void updateAddress(String diachi) {
        // Kiểm tra nếu binding đã được khởi tạo
        if (binding != null) {
            binding.editTextDC.setText(diachi);
        }
    }

}
