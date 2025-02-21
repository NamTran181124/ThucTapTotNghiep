package com.example.hotrovieclam.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hotrovieclam.Nam.RegisterEmployer;
import com.example.hotrovieclam.Fragment.Child_Fragment.ChangPassWordFragment;
import com.example.hotrovieclam.Fragment.Child_Fragment.DialogFragmentAvatar;
import com.example.hotrovieclam.Fragment.Child_Fragment.MyProFileFragment;
import com.example.hotrovieclam.Model.UserSessionManager;
import com.example.hotrovieclam.Nam.login.Login;
import com.example.hotrovieclam.R;
import com.example.hotrovieclam.databinding.FragmentAcountBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AcountFragment extends Fragment {

    private FragmentAcountBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_buttom);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
        // Inflate the layout for this fragment
        binding = FragmentAcountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProFileFragment myProFileFragment = new MyProFileFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, myProFileFragment).addToBackStack("null").commit();
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_buttom);
                if (bottomNav != null) {
                    bottomNav.setVisibility(View.GONE);
                }

            }
        });
        binding.llHoSoUngTuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), Application_candidate.class);
                startActivity(intent);
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đăng xuất người dùng khỏi Firebase
                FirebaseAuth.getInstance().signOut();

                // Xóa UID khỏi SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("user_uid");
                editor.apply();

                // Chuyển về màn hình đăng nhập
                Intent intent = new Intent(getActivity(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

                Toast.makeText(getContext(), "log out ok l", Toast.LENGTH_SHORT).show();
            }
        });
        // Xử lý sự kiện khi người dùng nhấn vào nút "Acount".
        binding.acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang màn hình đăng ký nhà tuyển dụng.
                Intent i = new Intent(getActivity(), RegisterEmployer.class);
                startActivity(i);
            }
        });
        binding.doipassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangPassWordFragment changePassWord = new ChangPassWordFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, changePassWord).addToBackStack("null").commit();
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_buttom);
                if (bottomNav != null) {
                    bottomNav.setVisibility(View.GONE);
                }
            }
        });

        binding.avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("000", "calll: ");
                DialogFragmentAvatar avatar = new DialogFragmentAvatar();
                avatar.show(getParentFragmentManager(), "ChangeAvatarDialog");
            }
        });
        getParentFragmentManager().setFragmentResultListener("updateSuccess", this, (req, keyvalue) -> {
            boolean update = keyvalue.getBoolean("update");
            if (update) {
                binding.avt.setVisibility(View.GONE);
                HienThiThongTin();

            }
        });
        HienThiThongTin();
        binding.avt.setImageResource(R.drawable.no_company);
        return view;

    }

    public void HienThiThongTin() {
        UserSessionManager sessionManager = new UserSessionManager();
        String uid = sessionManager.getUserUid();
        binding.load.setVisibility(View.VISIBLE);

        // Dùng UID để lắng nghe dữ liệu người dùng từ Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(uid);

        // Lắng nghe thay đổi từ Firestore
        docRef.addSnapshotListener((documentSnapshot, error) -> {
            if (error != null) {
                Log.d("Firestore", "Lỗi khi lắng nghe dữ liệu.", error);
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String phoneNumber = documentSnapshot.getString("phoneNumber");
                Long typeuser = documentSnapshot.getLong("userTypeId");

                if (typeuser != null && typeuser == 2) {
                    binding.mucNhaTuynDung.setVisibility(View.VISIBLE);
                }

                // Hiển thị thông tin người dùng
                binding.name.setText(name);
                binding.email.setText(email);
                binding.sdt.setText(phoneNumber);
                String avatarUrl = documentSnapshot.getString("avatar");

                // Kiểm tra xem Fragment đã gắn vào Activity hay chưa trước khi dùng Glide
                if (isAdded() && avatarUrl != null && !avatarUrl.isEmpty()) {
                    // Dùng Glide để tải ảnh từ URL và hiển thị vào ImageView
                    Glide.with(requireContext())
                            .load(avatarUrl)
                            .centerCrop()
                            .into(binding.avt);
                    binding.avt.setVisibility(View.VISIBLE);
                } else {
                    binding.avt.setImageResource(R.drawable.no_company);
                }

                binding.load.setVisibility(View.GONE);

                Log.d("Firestore", "Dữ liệu cập nhật: " + email + ", " + name);
            } else {
                Log.d("Firestore", "Không tìm thấy dữ liệu người dùng.");
            }
        });
    }

}