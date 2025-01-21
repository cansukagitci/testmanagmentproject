-- V2__Create_issues_table.sql
CREATE TABLE issues (
    id SERIAL PRIMARY KEY,                -- Otomatik artan benzersiz ID
    issue_title VARCHAR(255) NOT NULL,    -- Issue başlığı
    issue_type VARCHAR(100) NOT NULL,      -- Issue tipi
    description TEXT,                      -- Issue açıklaması
    issue_item VARCHAR(255),           -- Issue yazılan yer
    due_date DATE,                         -- Son tarih
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Oluşturulma tarihi
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- Güncellenme tarihi
    is_deleted BOOLEAN DEFAULT FALSE         -- Silinmiş durumu
);