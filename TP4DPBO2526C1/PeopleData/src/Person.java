// class Person -> merepresentasikan satu orang di TIX.ID
// (karyawan, pelanggan, member, kru film, atau tamu undangan)
public class Person {
    // 4 atribut awal sesuai ketentuan
    private String id;
    private String nama;
    private int tahunLahir;
    private String kategori;

    // atribut baru (ditambahkan sesuai poin 3 tugas)
    private int tinggiBadan; // dalam cm, diisi lewat JSlider

    // Constructor
    public Person() {
    }

    public Person(String id, String nama, int tahunLahir, String kategori, int tinggiBadan) {
        this.id = id;
        this.nama = nama;
        this.tahunLahir = tahunLahir;
        this.kategori = kategori;
        this.tinggiBadan = tinggiBadan;
    }

    // Getter dan Setter untuk id
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // Getter dan Setter untuk nama
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    // Getter dan Setter untuk tahunLahir
    public int getTahunLahir() { return tahunLahir; }
    public void setTahunLahir(int tahunLahir) { this.tahunLahir = tahunLahir; }

    // Getter dan Setter untuk kategori
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    // Getter dan Setter untuk tinggiBadan (atribut baru)
    public int getTinggiBadan() { return tinggiBadan; }
    public void setTinggiBadan(int tinggiBadan) { this.tinggiBadan = tinggiBadan; }
}
