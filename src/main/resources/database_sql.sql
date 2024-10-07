-- Create ArticleStatus table
CREATE TABLE ArticleStatus (
    id INT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(20) NOT NULL UNIQUE
);

-- Create Author table
CREATE TABLE Author (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    birthdate DATE NOT NULL,
    role ENUM('Contributor', 'Editor') NOT NULL
);

-- Create Article table
CREATE TABLE Article (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    creation_date DATETIME NOT NULL,
    publication_date DATETIME,
    status_id INT NOT NULL,
    author_id INT NOT NULL,
    FOREIGN KEY (status_id) REFERENCES ArticleStatus(id),
    FOREIGN KEY (author_id) REFERENCES Author(id)
);

-- Create Comment table
CREATE TABLE Comment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    creation_date DATETIME NOT NULL,
    status ENUM('approved', 'rejected') NOT NULL,
    article_id INT NOT NULL,
    FOREIGN KEY (article_id) REFERENCES Article(id)
);

-- Insert sample data into ArticleStatus
INSERT INTO ArticleStatus (status) VALUES
('draft'),
('published'),
('archived'),
('under_review');

-- Insert sample data into Author
INSERT INTO Author (name, email, birthdate, role) VALUES
('John Doe', 'john.doe@example.com', '1985-05-15', 'Contributor'),
('Jane Smith', 'jane.smith@example.com', '1990-08-22', 'Editor'),
('Alice Johnson', 'alice.johnson@example.com', '1988-03-10', 'Contributor'),
('Bob Williams', 'bob.williams@example.com', '1982-11-30', 'Editor');

-- Insert sample data into Article
INSERT INTO Article (title, content, creation_date, publication_date, status_id, author_id) VALUES
('First Blog Post', 'This is the content of the first blog post.', '2023-05-01 10:00:00', '2023-05-02 12:00:00', 2, 1),
('Draft Article', 'This article is still in draft.', '2023-05-03 09:00:00', NULL, 1, 2),
('Tech News', 'Latest updates in the tech world.', '2023-05-04 11:00:00', '2023-05-05 10:00:00', 2, 3),
('Archived Post', 'This post has been archived.', '2023-04-15 14:00:00', '2023-04-16 09:00:00', 3, 4);

-- Insert sample data into Comment
INSERT INTO Comment (content, creation_date, status, article_id) VALUES
('Great article!', '2023-05-02 13:00:00', 'approved', 1),
('I found this very informative.', '2023-05-02 14:30:00', 'approved', 1),
('Looking forward to the full article.', '2023-05-03 10:00:00', 'approved', 2),
('Interesting perspective on tech trends.', '2023-05-05 11:00:00', 'approved', 3);

-- Add indexes
CREATE INDEX idx_article_title ON Article(title);
CREATE INDEX idx_article_creation_date ON Article(creation_date);
CREATE INDEX idx_comment_creation_date ON Comment(creation_date);

-- Grant privileges (adjust as needed for your specific database user)
GRANT SELECT, INSERT, UPDATE, DELETE ON blogapp.* TO 'root'@'localhost';