CREATE TABLE transactions (
                              id INTEGER PRIMARY KEY AUTOINCREMENT,
                              create_time DATETIME DEFAULT NULL,
                              update_time DATETIME DEFAULT NULL,
                              source TEXT DEFAULT NULL,
                              username TEXT DEFAULT NULL,
                              trans_time DATETIME DEFAULT NULL,
                              category TEXT DEFAULT NULL,
                              trans_to_name TEXT DEFAULT NULL,
                              trans_to_account TEXT DEFAULT NULL,
                              product_desc TEXT DEFAULT NULL,
                              trans_type TEXT DEFAULT NULL,
                              amount REAL DEFAULT NULL,
                              pay_method TEXT DEFAULT NULL,
                              trans_status TEXT DEFAULT NULL,
                              trans_order_no TEXT DEFAULT NULL,
                              merchant_order_no TEXT DEFAULT NULL,
                              memo TEXT DEFAULT NULL,
                              tags TEXT DEFAULT NULL
);
