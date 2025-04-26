import pymysql

# Configuração do banco de dados
db_config = {
    'host': 'localhost',
    'user': 'root',
    'password': '',  # Sem senha
    'db': 'obras_db',  # Nome do banco de dados
    'charset': 'utf8mb4',
    'cursorclass': pymysql.cursors.DictCursor
}

def get_db_connection():
    """Estabelece e retorna uma conexão com o banco de dados."""
    conn = pymysql.connect(
        host=db_config['host'],
        user=db_config['user'],
        password=db_config['password'],
        database=db_config['db'],  # Especifica o banco de dados
        charset=db_config['charset'],
        cursorclass=db_config['cursorclass']
    )
    return conn

def setup_database():
    # Conecta ao MySQL (sem especificar um banco de dados)
    conn = pymysql.connect(
        host=db_config['host'],
        user=db_config['user'],
        password=db_config['password'],
        charset=db_config['charset'],
        cursorclass=db_config['cursorclass']
    )cs
    
    try:
        with conn.cursor() as cursor:
            # Cria o banco de dados 'obras_db' se não existir
            cursor.execute("DROP DATABASE IF EXISTS obras_db")
            cursor.execute("CREATE DATABASE obras_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
            print("Banco de dados 'obras_db' criado com sucesso!")
            
            # Seleciona o banco de dados 'obras_db'
            cursor.execute("USE obras_db")
            
            # Cria a tabela 'obras'
            cursor.execute("""
            CREATE TABLE IF NOT EXISTS obras (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(100) NOT NULL,
                descricao TEXT NOT NULL,
                valor_estimado DECIMAL(10, 2) NOT NULL,
                lucro_estimado DECIMAL(10, 2) NOT NULL,
                status ENUM('Em andamento', 'Finalizada') DEFAULT 'Em andamento',
                data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """)
            print("Tabela 'obras' criada com sucesso!")
            
            # Cria a tabela 'gastos'
            cursor.execute("""
            CREATE TABLE IF NOT EXISTS gastos (
                id INT AUTO_INCREMENT PRIMARY KEY,
                tipo VARCHAR(50) NOT NULL,
                valor DECIMAL(10, 2) NOT NULL,
                observacao TEXT,
                obra_id INT NOT NULL,
                data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (obra_id) REFERENCES obras(id) ON DELETE CASCADE
            )
            """)
            print("Tabela 'gastos' criada com sucesso!")
            
            conn.commit()
        print("Configuração do banco de dados concluída com sucesso!")
        
    except Exception as e:
        print(f"Erro ao configurar o banco de dados: {e}")
    
    finally:
        conn.close()

if __name__ == "__main__":
    setup_database()
