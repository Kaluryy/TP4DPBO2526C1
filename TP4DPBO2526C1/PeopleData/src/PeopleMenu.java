import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Year;
import java.util.ArrayList;

public class PeopleMenu extends JFrame {
    // ===== komponen form (nama harus sama dengan "field name" di PeopleMenu.form) =====
    private JPanel mainPanel;
    private JTable peopleTable;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;
    private JSlider tinggiSlider;          // komponen untuk atribut baru (bukan JTextField -> bonus)
    private JLabel tinggiValueLabel;       // menampilkan nilai slider saat ini
    private JButton addUpdateButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private JLabel titleLabel;

    // ===== data =====
    private ArrayList<Person> listPerson;
    private int selectedIndex = -1;        // -1 artinya tidak ada baris yang dipilih

    private static final int TINGGI_MIN = 100;
    private static final int TINGGI_MAX = 220;
    private static final int TINGGI_DEFAULT = 165;

    // main method
    public static void main(String[] args) {
        // buat object window
        PeopleMenu menu = new PeopleMenu();

        // atur ukuran window
        menu.setSize(700, 620);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // constructor
    public PeopleMenu() {
        // inisialisasi listPerson
        listPerson = new ArrayList<>();

        // isi listPerson
        populateList();

        // isi tabel orang
        peopleTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] kategoriData = { "???", "Karyawan", "Pelanggan", "Member VIP", "Kru Film", "Tamu Undangan" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // atur slider tinggi badan (atribut baru)
        tinggiSlider.setMinimum(TINGGI_MIN);
        tinggiSlider.setMaximum(TINGGI_MAX);
        tinggiSlider.setValue(TINGGI_DEFAULT);
        tinggiValueLabel.setText(TINGGI_DEFAULT + " cm");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat slider digeser, label nilainya ikut berubah
        tinggiSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                tinggiValueLabel.setText(tinggiSlider.getValue() + " cm");
            }
        });

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    return;
                }

                // konfirmasi sebelum menghapus data
                String nama = listPerson.get(selectedIndex).getNama();
                int pilihan = JOptionPane.showConfirmDialog(
                        PeopleMenu.this,
                        "Yakin ingin menghapus data \"" + nama + "\"?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                // hapus hanya kalau user memilih Yes
                if (pilihan == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = peopleTable.getSelectedRow();
                if (selectedIndex == -1) {
                    return;
                }

                // ambil data orang di baris tersebut
                // (urutan baris tabel = urutan di listPerson karena tabel tidak di-sort)
                Person p = listPerson.get(selectedIndex);

                // ubah isi textfield, combo box, dan slider
                idField.setText(p.getId());
                namaField.setText(p.getNama());
                tahunLahirField.setText(String.valueOf(p.getTahunLahir()));
                kategoriComboBox.setSelectedItem(p.getKategori());
                tinggiSlider.setValue(p.getTinggiBadan());

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    // method untuk mengisi data awal (hardcoded)
    private void populateList() {
        listPerson.add(new Person("P001", "Andi Wijaksana", 1992, "Karyawan", 172));
        listPerson.add(new Person("P002", "Bella Kusuma", 1997, "Karyawan", 160));
        listPerson.add(new Person("P003", "Candra Permana", 1995, "Pelanggan", 175));
        listPerson.add(new Person("P004", "Dinda Maharani", 1998, "Member VIP", 158));
        listPerson.add(new Person("P005", "Eko Prasetyo", 2001, "Pelanggan", 170));
        listPerson.add(new Person("P006", "Farah Nabila", 1994, "Karyawan", 163));
        listPerson.add(new Person("P007", "Gilang Saputra", 1999, "Kru Film", 178));
        listPerson.add(new Person("P008", "Hana Safitri", 2002, "Member VIP", 155));
        listPerson.add(new Person("P009", "Ivan Hakim", 1990, "Tamu Undangan", 180));
        listPerson.add(new Person("P010", "Jasmine Putri", 2003, "Pelanggan", 162));
    }

    // method untuk membuat model tabel dari listPerson
    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = { "No", "ID", "Nama", "Tahun Lahir", "Kategori", "Tinggi (cm)" };

        // buat objek tabel dengan kolom yang sudah dibuat
        // (isCellEditable dibuat false supaya sel tabel tidak bisa diedit langsung)
        DefaultTableModel tmp = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // isi tabel dengan listPerson
        for (int i = 0; i < listPerson.size(); i++) {
            Person p = listPerson.get(i);
            Object[] row = {
                    i + 1,
                    p.getId(),
                    p.getNama(),
                    p.getTahunLahir(),
                    p.getKategori(),
                    p.getTinggiBadan()
            };
            tmp.addRow(row);
        }

        return tmp;
    }

    // CREATE
    public void insertData() {
        try {
            // ambil value dari textfield, combobox, dan slider
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            int tahunLahir = Integer.parseInt(tahunLahirField.getText().trim());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int tinggiBadan = tinggiSlider.getValue();

            // validasi input, kalau tidak valid batalkan insert
            if (!isInputValid(id, nama, tahunLahir, -1)) {
                return;
            }

            // tambahkan data ke dalam list
            listPerson.add(new Person(id, nama, tahunLahir, kategori, tinggiBadan));

            // update tabel
            peopleTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tahun lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE
    public void updateData() {
        try {
            // ambil data dari form
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            int tahunLahir = Integer.parseInt(tahunLahirField.getText().trim());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int tinggiBadan = tinggiSlider.getValue();

            // validasi input (baris yang sedang diedit tidak ikut dicek untuk ID kembar)
            if (!isInputValid(id, nama, tahunLahir, selectedIndex)) {
                return;
            }

            // ubah data orang di list
            listPerson.get(selectedIndex).setId(id);
            listPerson.get(selectedIndex).setNama(nama);
            listPerson.get(selectedIndex).setTahunLahir(tahunLahir);
            listPerson.get(selectedIndex).setKategori(kategori);
            listPerson.get(selectedIndex).setTinggiBadan(tinggiBadan);

            // update tabel
            peopleTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(this, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tahun lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE (dipanggil setelah user menekan Yes di dialog konfirmasi)
    public void deleteData() {
        // hapus data dari list
        listPerson.remove(selectedIndex);

        // update tabel
        peopleTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
    }

    // method untuk mengosongkan form
    public void clearForm() {
        // kosongkan semua textfield, combo box, dan slider
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        tinggiSlider.setValue(TINGGI_DEFAULT);

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // hilangkan highlight baris yang dipilih di tabel
        peopleTable.clearSelection();

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }

    // method untuk memvalidasi input form
    // ignoreIndex = index data yang diabaikan saat cek ID kembar (-1 kalau insert)
    private boolean isInputValid(String id, String nama, int tahunLahir, int ignoreIndex) {
        // ID dan nama tidak boleh kosong
        if (id.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID dan Nama tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // tahun lahir harus masuk akal
        int tahunSekarang = Year.now().getValue();
        if (tahunLahir < 1900 || tahunLahir > tahunSekarang) {
            JOptionPane.showMessageDialog(this, "Tahun lahir harus antara 1900 dan " + tahunSekarang + "!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kategori harus dipilih (index 0 adalah "???")
        if (kategoriComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Pilih kategori terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // ID tidak boleh kembar dengan data lain
        for (int i = 0; i < listPerson.size(); i++) {
            if (i != ignoreIndex && listPerson.get(i).getId().equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(this, "ID \"" + id + "\" sudah dipakai!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }
}
