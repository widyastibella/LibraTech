/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libratech;

import java.util.ArrayList;


public class AnggotaPerpustakaan {
    private String nama;
    private int nomorAnggota;
    private String alamat;
    private String riwayatPeminjaman;
    private int denda;
    
    public AnggotaPerpustakaan(String nama, int nomorAnggota, String alamat, String riwayatPeminjaman, int denda) {
        this.nama = nama;
        this.nomorAnggota = nomorAnggota;
        this.alamat = alamat;
        this.riwayatPeminjaman = riwayatPeminjaman;
        this.denda = denda;
    }
    
    
    private TransaksiPeminjaman transaksi;
    private Admin admin;
    public AnggotaPerpustakaan(Admin admin, String nama, int nomorAnggota, String alamat, String riwayatPeminjaman, int denda,TransaksiPeminjaman transaksi) {
        this.admin = admin;
        this.nama = nama;
        this.nomorAnggota = nomorAnggota;
        this.alamat = alamat;
        this.riwayatPeminjaman = riwayatPeminjaman;
        this.denda = denda;
        this.transaksi = transaksi;
        admin.addAnggota(this);
    }
    
    
    public void melihatBuku(){
        System.out.println("============================= Daftar Buku ===============================\n");
        for(Buku value : admin.daftarbuku){
            System.out.println("Judul        : " + value.getJudul());
            System.out.println("Pengarang    : "+value.getPengarang());
            System.out.println("NomorISBN    : "+value.getNomorISBN());
            System.out.println("Ketersediaan : "+value.isStatusKetersediaan());
            System.out.println(" ");
        }
        System.out.println("=========================================================================\n");
    }
    
    
    public void cariBuku(String judul){
        boolean ditemukan = false;
        for(Buku value : admin.daftarbuku){
            if (value.getJudul().contains(judul)){
               System.out.println("Judul        : " + value.getJudul());
               System.out.println("Pengarang    : "+value.getPengarang());
               System.out.println("NomorISBN    : "+value.getNomorISBN());
               System.out.println("Ketersediaan : "+value.isStatusKetersediaan());
               System.out.println(" ");
               ditemukan = true;
               break;
            }
        }
        if(!ditemukan){
            System.out.println(judul+ " Tidak Ditemukan");
        }
    }
    
    private int totalPinjaman;
    private TransaksiPengembalian riwayatTransaksi;
    ArrayList<TransaksiPengembalian> daftarTransaksi = new ArrayList<>();
    StringBuilder rekapTransaksi = new StringBuilder();
    ArrayList<Buku> bukuPinjaman = new ArrayList<>();
    
    public void pinjamBuku(String judul){
        StringBuilder pinjam = new StringBuilder();
        boolean ditemukan = false;
        System.out.println("\n=========================== Peminjaman Buku =============================");
        for (Buku value : admin.daftarbuku){
            if (value.getJudul().equals(judul) && value.isStatusKetersediaan()){
                pinjam.append("Peminjam          : "+ nama).append("\nNomor Anggota     : " + nomorAnggota);
                pinjam.append("\nMeminjam Buku     : " + value.getJudul());
                bukuPinjaman.add(value);
                //admin.daftarbuku.remove(value);
                value.setStatusKetersediaan(false);
                ditemukan = true;
                rekapTransaksi.append(judul + ", ");
                riwayatPeminjaman = rekapTransaksi.toString();
                this.totalPinjaman += 1;
                break;
            }
        }
        if(ditemukan){
            transaksi.setIdTransaksi(transaksi.getIdTransaksi() + 1);
            pinjam.append("\nId Transaksi      : "+ transaksi.getIdTransaksi()).append("\nWaktu Peminjaman  : " + transaksi.getWaktuPeminjaman())
              .append(" Durasi Max Peminjaman : " + transaksi.getDurasiPeminjaman() + " Hari\n").append("\nH-3 Jadwal Buku Dikembalikan!\n");
            riwayatTransaksi = new TransaksiPengembalian(judul, transaksi.getIdTransaksi(), transaksi.getWaktuPeminjaman(), denda);
            ditemukan = false;
            daftarTransaksi.add(riwayatTransaksi);
            Notifikasi aktivitas = new Notifikasi(riwayatTransaksi, transaksi, this, this);
            admin.aktivitas.add(aktivitas);
        }else{
            System.out.print(judul + " Tidak Ditemukan\n");
        }
        System.out.println(pinjam.toString());
    }

    
    public void mengembalikanBuku(String judul, int durasiPengembalian){
        boolean berhasil = false;
        for(Buku value  : bukuPinjaman){
            if(value.getJudul().equals(judul)){
//                admin.daftarbuku.add(value);
                value.setStatusKetersediaan(true);
                bukuPinjaman.remove(value);
                berhasil = true;
                break;
            }else{
                System.out.println("Tidak Ada Buku Yang Sesuai!");
            }
        }
        if (berhasil){
            System.out.println("========================== Pengembalian Buku ============================");
            System.out.println("Nama             : "+nama + "\nID               : " + nomorAnggota);
            System.out.println("Berhasil Mengembalikan Buku " + judul);
            System.out.println("Waktu Peminjaman : " + transaksi.getWaktuPeminjaman());
            System.out.print("Durasi Peminjaman " + durasiPengembalian + " Hari");
            if(transaksi.getDurasiPeminjaman() < durasiPengembalian){
                if(durasiPengembalian - transaksi.getDurasiPeminjaman() == 1){
                    this.denda += riwayatTransaksi.getTotalDenda();
                    System.out.println(" Dengan Denda " + riwayatTransaksi.getTotalDenda() + "K\n");
                }else if(durasiPengembalian - transaksi.getDurasiPeminjaman() == 2){
                    this.denda += (riwayatTransaksi.getTotalDenda() * 2);
                    System.out.println(" Dengan Denda " + (riwayatTransaksi.getTotalDenda()*2) + "K\n");
                }else if(durasiPengembalian - transaksi.getDurasiPeminjaman() == 3){
                    this.denda += (riwayatTransaksi.getTotalDenda() * 3);
                    System.out.println(" Dengan Denda " + (riwayatTransaksi.getTotalDenda()*3) + "K\n");
                }else if(durasiPengembalian - transaksi.getDurasiPeminjaman() == 4){
                    this.denda += (riwayatTransaksi.getTotalDenda() * 4);
                    System.out.println(" Dengan Denda " + (riwayatTransaksi.getTotalDenda()*4) + "K\n");
                }else if(durasiPengembalian - transaksi.getDurasiPeminjaman() == 5){
                    this.denda += (riwayatTransaksi.getTotalDenda() * 5);
                    System.out.println(" Dengan Denda " + (riwayatTransaksi.getTotalDenda()*5) + "K\n");
                }else{
                    this.denda += (riwayatTransaksi.getTotalDenda() * 50);
                    System.out.println(" Dengan Denda " + (riwayatTransaksi.getTotalDenda()*50) + "K\n");
                }
            }else{
                System.out.println(" Dan Tidak Ada Denda\n");
            }
            berhasil = false;
        }
    }
    
    
    public void tampilkanTransaksi(){
        System.out.println("=========================== Total Transaksi =============================");
        System.out.println("Total Transaksi " + nama);;
        System.out.print("Buku Yang Di Pinjam : \n");
        for(TransaksiPengembalian value : daftarTransaksi){
            System.out.print(value.getJudul() + " " + value.getIdTransaksi());
            System.out.print(" Pada : " + value.getWaktuPeminjaman());
            System.out.println();
        }
    }
    
    
    public String getNama() {
        return nama;
    }

    public int getNomorAnggota() {
        return nomorAnggota;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getRiwayatPeminjaman() {
        return riwayatPeminjaman;
    }

    public int getDenda() {
        return denda;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNomorAnggota(int nomorAnggota) {
        this.nomorAnggota = nomorAnggota;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setRiwayatPeminjaman(String riwayatPeminjaman) {
        this.riwayatPeminjaman = riwayatPeminjaman;
    }

    public void setDenda(int denda) {
        this.denda = denda;
    }

    public int getTotalPinjaman() {
        return totalPinjaman;
    }

    public void setTotalPinjaman(int totalPinjaman) {
        this.totalPinjaman = totalPinjaman;
    }
    
    public String toString() {
        return "Nama: " + this.nama + " Nomor Anggota: " + this.nomorAnggota;
    }
}
