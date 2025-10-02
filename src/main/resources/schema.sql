SET NAMES utf8mb4;

-- User Table
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(190) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    profileUrl VARCHAR(255) NULL,
    role VARCHAR(20)   NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Category Table
CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Stack Table
CREATE TABLE IF NOT EXISTS stack (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Post Table (메타 정보)
CREATE TABLE IF NOT EXISTS post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    summary VARCHAR(500) NOT NULL,
    downloads INT DEFAULT 0 NOT NULL,
    likes INT DEFAULT 0 NOT NULL,
    views INT DEFAULT 0 NOT NULL,
    is_public BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_category FOREIGN KEY (category_id) REFERENCES category(id),

    KEY idx_post_cat_created (category_id, created_at DESC),
    KEY idx_post_popular (downloads DESC, created_at DESC),
    KEY idx_post_likes (likes DESC, created_at DESC)
);

-- Post Stack Table (다대다 관계)
CREATE TABLE IF NOT EXISTS post_stack (
    post_id BIGINT NOT NULL,
    stack_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, stack_id),

    CONSTRAINT fk_post_stack_post FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_stack_stack FOREIGN KEY (stack_id) REFERENCES stack(id) ON DELETE CASCADE,

    KEY idx_poststack_stack (stack_id, post_id)
);

-- Post Detail Table (본문/첨부)
CREATE TABLE IF NOT EXISTS post_detail (
    post_id BIGINT PRIMARY KEY,
    upload_type VARCHAR(10) NOT NULL, -- "ZIP" or "SNIPPET"
    readme_md MEDIUMTEXT NULL,
    snippet_text MEDIUMTEXT NULL,
    zip_key VARCHAR(255) NULL,
    zip_file_name VARCHAR(255) NULL,
    zip_file_size BIGINT NULL,
    runtime VARCHAR(50) NULL,
    version VARCHAR(20) NULL,

    FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
);

-- Reaction Table
CREATE TABLE IF NOT EXISTS reaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    type VARCHAR(10) NOT NULL DEFAULT 'LIKE', -- 추후 확장(like, bookmark ..)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reaction_post FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE,
    CONSTRAINT fk_reaction_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,

    UNIQUE KEY uk_reaction_unique (post_id, user_id, type),
    KEY idx_reaction_post (post_id, created_at DESC),
    KEY idx_reaction_user (user_id, created_at DESC)
);

-- Comment Table
CREATE TABLE IF NOT EXISTS comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    parent_id BIGINT NULL,
    root_id BIGINT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_parent FOREIGN KEY (parent_id) REFERENCES comment(id) ON DELETE CASCADE,

    KEY idx_comment_post (post_id, created_at DESC),
    KEY idx_comment_parent_created (parent_id, created_at DESC),
    KEY idx_comment_root_created (root_id, created_at DESC)
);