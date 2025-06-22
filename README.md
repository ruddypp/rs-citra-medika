# Aplikasi Sistem Antrian Rumah Sakit

![Logo Aplikasi](app/src/main/res/drawable/app_logo.xml)

## Deskripsi

Aplikasi Sistem Antrian Rumah Sakit adalah aplikasi Android yang dirancang untuk memudahkan manajemen antrian pasien di rumah sakit. Aplikasi ini memungkinkan pasien untuk mendaftar secara mandiri dan mendapatkan nomor antrian, sementara staf rumah sakit dapat mengelola dan memanggil pasien sesuai urutan.

## Fitur

### Untuk Pasien
- Pendaftaran nomor antrian dengan mengisi NIK, nama, dan kebutuhan
- Pemilihan poli yang diinginkan (Poli Umum, Poli Gigi, Poli KIA)
- Melihat informasi nomor antrian yang didapatkan
- Antarmuka yang user-friendly dan modern

### Untuk Staf
- Login dengan PIN khusus staf
- Dashboard untuk melihat daftar antrian pada setiap poli
- Memanggil pasien dan menandai pasien yang sudah selesai dilayani
- Manajemen antrian yang efisien

## Teknologi yang Digunakan

- Kotlin
- Android Jetpack (Navigation Component, ViewModel, LiveData)
- SQLite Database
- Material Design Components
- ViewBinding
- Fragment-based UI

## Struktur Proyek

```
ticketing/
  - app/
    - src/
      - main/
        - java/com/example/ticketing/
          - adapter/            # Adapter untuk RecyclerView
          - data/               # Database helper dan operasi data
          - model/              # Model data
          - [Fragment files]    # UI fragments
          - MainActivity.kt     # Activity utama
        - res/
          - drawable/           # Gambar dan ikon
          - layout/             # Layout XML
          - navigation/         # Navigation graph
          - values/             # Strings, colors, styles
```

## Cara Menggunakan

### Instalasi
1. Clone repository ini
2. Buka dengan Android Studio
3. Build dan jalankan aplikasi pada emulator atau perangkat Android

### Penggunaan untuk Pasien
1. Buka aplikasi dan tekan tombol "Get Started"
2. Isi formulir pendaftaran dengan NIK, nama, dan kebutuhan
3. Pilih poli yang diinginkan dari dropdown
4. Tekan tombol "Dapatkan Nomor Antrian"
5. Lihat informasi nomor antrian yang didapatkan

### Penggunaan untuk Staf
1. Pada halaman utama, tekan dan tahan nama rumah sakit selama beberapa detik
2. Masukkan PIN staf (default: 123456)
3. Pada dashboard staf, pilih tab poli yang ingin dikelola
4. Gunakan tombol "Panggil" untuk memanggil pasien
5. Gunakan tombol "Selesai" setelah pasien dilayani

## Desain UI

Aplikasi ini menggunakan desain Material Design dengan tema warna biru sebagai warna utama. Tampilan dirancang untuk memberikan pengalaman pengguna yang menarik dan mudah digunakan, dengan fokus pada:

- Kartu (cards) untuk menampilkan informasi
- Tombol yang mudah dilihat dan diakses
- Formulir input yang jelas
- Tab untuk navigasi antar poli
- Status visual yang jelas untuk antrian

## Pengembangan Lebih Lanjut

Beberapa ide untuk pengembangan aplikasi di masa depan:
- Integrasi dengan sistem rumah sakit yang sudah ada
- Notifikasi untuk pasien saat giliran hampir tiba
- Estimasi waktu tunggu
- Riwayat kunjungan pasien
- Sistem feedback pasien
- Dukungan multi-bahasa

## Lisensi

© 2023 Aplikasi Sistem Antrian Rumah Sakit. All rights reserved. 
