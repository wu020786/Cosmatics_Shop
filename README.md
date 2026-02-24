# L'ÉCLAT - Luxury Skincare Shopping System (頂級保養品購物系統)

L'ÉCLAT 是一個基於 Java Swing 與 MySQL 開發的桌面端（GUI）購物與銷售管理系統。本系統採用分層架構（Controller, Service, DAO, Model）設計，具備完善的客戶端購物流程以及員工/店長端的業績管理與報表分析功能。

## 🌟 核心功能 (Features)

系統分為「客戶專區 (Customer)」與「員工入口 (Employee Portal)」兩大模組，根據登入身分提供不同功能：

### 🛍️ 客戶端 (Customer Portal)
* **會員系統**：支援新會員註冊與登入。
* **購物商城**：瀏覽產品清單，選購商品並可在結帳時「指定銷售顧問 (Advisor)」。
* **電子收據**：結帳後自動產生明細電子收據，並支援列印功能。
* **訂單管理**：查詢歷史訂單、取消訂單，以及進入「修改訂單模式」重新計算數量與總額。

### 💼 員工與管理端 (Employee & Executive Portal)
* **雙重權限機制**：一般業務員與店長（預設代號：`S001`）登入後有不同的權限視角。
* **專屬業績儀表板 (Sales Performance)**：一般業務可查看個人總營收、成交單數、客單價 (AOV) 及近期銷售紀錄。
* **店長進階報表 (Executive Dashboard)**：
  * 支援時間區間篩選（Start Date / End Date）。
  * 支援多維度數據分析（整體表現、依產品分類、依單一產品）。
  * 動態排行榜與營業總額統計。
* **綜合訂單查詢 (Order Query System)**：支援雙重連動下拉選單與正規表達式關鍵字，快速過濾/搜尋特定訂單。

## 🛠️ 技術棧 (Tech Stack)

* **程式語言**: Java (JDK 8 或以上)
* **圖形介面**: Java Swing, AWT
* **資料庫**: MySQL (JDBC Driver: `com.mysql.cj.jdbc.Driver`)
* **架構設計**: 
  * **Model**: 實體資料模型 (Customer, Product, Order, OrderDetail, Sales)
  * **DAO (Data Access Object)**: 負責與資料庫互動 (CRUD 操作)
  * **Service**: 處理核心商業邏輯 (如密碼驗證、報表數據計算、訂單合併與修改)
  * **Controller (UI)**: 處理視窗畫面與使用者互動

## 📂 專案架構 (Project Structure)

```text
├── controller/         # UI 視窗與事件控制 (LoginUI, ShoppingUI, ManagerPerformanceUI...)
├── service/            # 商業邏輯層介面與實作 (CustomerService, OrderService...)
├── dao/                # 資料庫操作層介面與實作 (CustomerDao, OrderDao...)
├── model/              # 資料實體類別 (Java Beans)
└── util/               # 工具類別 (Tool.java - 負責建立資料庫連線)
