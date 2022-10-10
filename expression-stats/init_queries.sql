CREATE TABLE filestats (
    filename TEXT PRIMARY KEY,
    compiles INTEGER,
    chars INTEGER CHECK (chars >= 0),
    tokens INTEGER CHECK (tokens >= 0),
    classes INTEGER CHECK (classes >= 0),
    methods INTEGER CHECK (methods >= 0),
    nodes INTEGER CHECK (nodes >= 0)
);

CREATE TABLE exprstats (
    filename TEXT,
    path_expr TEXT,
    path_expr_root TEXT,
    ast_node TEXT,
    declaring_node TEXT,
    depth INTEGER CHECK (depth >= 0),
    chars INTEGER CHECK (chars >= 0),
    tokens INTEGER CHECK (tokens >= 0),
    PRIMARY KEY (filename, path_expr)
);

CREATE TABLE projects (
    project_path TEXT,
    filename TEXT PRIMARY KEY
);
