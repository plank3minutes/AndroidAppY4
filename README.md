# 📘 Đặc tả Hệ thống

**Tên dự án:** Ứng dụng Học tập Offline trên Android (Java)

---

## 1. Giới thiệu

Ứng dụng hướng đến việc hỗ trợ học tập cho người dùng thông qua các khóa học, bài học và bài quiz có sẵn mà **không cần kết nối mạng**.
Công nghệ sử dụng: **Android Java**, kiến trúc **MVVM**, `Fragment` với `Bottom Navigation`.

---

## 2. Mục tiêu

* Cung cấp danh sách khóa học và bài học **offline**.
* Theo dõi **tiến độ học tập** và **kết quả quiz**.
* Cho phép người dùng **tùy chỉnh cài đặt** (theme, font size).
* Hỗ trợ tính năng **bookmark** và **tìm kiếm**.
* Đảm bảo **trải nghiệm mượt mà**, dễ sử dụng.

---

## 3. Kiến trúc Hệ thống

* **Pattern:** `MVVM (Model-View-ViewModel)`
* **Các thành phần chính:**

  * **Model:** `Course`, `Lesson`, `Quiz`, `Question`, `UserProgress`
  * **ViewModel:** Quản lý dữ liệu và trạng thái cho từng `Fragment`
  * **View (UI):** `Fragment`, `RecyclerView`, `Custom Views`
  * **Repository:** Đọc/ghi dữ liệu từ **JSON** hoặc **Room Database**

---

## 4. Yêu cầu Chức năng

| Mã  | Tên chức năng        | Mô tả chi tiết                                               |
| --- | -------------------- | ------------------------------------------------------------ |
| F1  | Hiển thị khóa học    | Danh sách các khóa học với `title`, mô tả ngắn, ảnh minh họa |
| F2  | Chi tiết khóa học    | Danh sách bài học, tiến độ, bookmark                         |
| F3  | Xem nội dung bài học | Gồm `text`, hình ảnh, video (nếu có)                         |
| F4  | Quiz                 | Trắc nghiệm nhiều lựa chọn, tính điểm, lưu kết quả           |
| F5  | Theo dõi tiến độ     | Lưu `lesson` cuối cùng, điểm quiz, ngày truy cập cuối        |
| F6  | Bookmark             | Đánh dấu bài học yêu thích                                   |
| F7  | Tìm kiếm             | Tìm khóa học/bài học theo từ khóa                            |
| F8  | Cài đặt              | Chọn `theme (light/dark)`, kích thước font, thông báo        |
| F9  | Backup & Restore     | Xuất/Nhập tiến độ học tập qua file `JSON`                    |
| F10 | Thông báo nhắc học   | Báo nhắc học hàng ngày (sử dụng `AlarmManager`)              |
