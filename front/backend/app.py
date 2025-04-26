from flask import Flask, render_template, request, redirect, url_for, flash
from flask_sqlalchemy import SQLAlchemy

# Configuração da aplicação e banco de dados
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///obras.db'  # Banco SQLite
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.secret_key = 'secret_key'  # Para usar flash messages
db = SQLAlchemy(app)

# Model para a obra
class Obra(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    nome = db.Column(db.String(100), nullable=False)
    descricao = db.Column(db.String(200), nullable=False)
    valor_estimado = db.Column(db.Float, nullable=False)
    lucro_estimado = db.Column(db.Float, nullable=False)
    status = db.Column(db.String(20), default='Em andamento')
    gastos = db.relationship('Gasto', backref='obra', lazy=True)

# Model para os gastos
class Gasto(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    tipo = db.Column(db.String(50), nullable=False)
    valor = db.Column(db.Float, nullable=False)
    observacao = db.Column(db.String(200), nullable=True)
    obra_id = db.Column(db.Integer, db.ForeignKey('obra.id'), nullable=False)

# Rota inicial
@app.route('/')
def index():
    obras = Obra.query.all()
    return render_template('index.html', obras=obras)

# Rota de Cadastro de Obras
@app.route('/cadastro_obra', methods=['GET', 'POST'])
def cadastro_obra():
    if request.method == 'POST':
        nome = request.form['nome']
        descricao = request.form['descricao']
        valor_estimado = request.form.get('valor_estimado', type=float)
        lucro_estimado = request.form.get('lucro_estimado', type=float)

        # Validação
        if not nome or not descricao or valor_estimado is None or lucro_estimado is None:
            flash("Todos os campos são obrigatórios!", "error")
            return redirect(url_for('cadastro_obra'))
        
        nova_obra = Obra(nome=nome, descricao=descricao, valor_estimado=valor_estimado, lucro_estimado=lucro_estimado)
        db.session.add(nova_obra)
        db.session.commit()

        flash("Obra cadastrada com sucesso!", "success")
        return redirect(url_for('index'))
    
    return render_template('cadastro_obra.html')

# Rota de Cadastro de Gastos
@app.route('/cadastro_gasto/<int:obra_id>', methods=['GET', 'POST'])
def cadastro_gasto(obra_id):
    obra = Obra.query.get_or_404(obra_id)

    if request.method == 'POST':
        tipo = request.form['tipo']
        valor = request.form.get('valor', type=float)
        observacao = request.form['observacao']

        # Validação
        if not tipo or valor is None:
            flash("O tipo e valor são obrigatórios!", "error")
            return redirect(url_for('cadastro_gasto', obra_id=obra.id))
        
        novo_gasto = Gasto(tipo=tipo, valor=valor, observacao=observacao, obra_id=obra.id)
        db.session.add(novo_gasto)
        db.session.commit()

        flash("Gasto cadastrado com sucesso!", "success")
        return redirect(url_for('relatorios'))
    
    return render_template('cadastro_gasto.html', obra=obra)

# Rota de Relatórios
@app.route('/relatorios')
def relatorios():
    obras = Obra.query.all()
    for obra in obras:
        # Calcula o total de gastos de cada obra
        total_gastos = sum(gasto.valor for gasto in obra.gastos)
        obra.total_gastos = total_gastos
        obra.lucro = obra.valor_estimado - total_gastos
    
    return render_template('relatorios.html', obras=obras)

if __name__ == '__main__':
    with app.app_context():  # Contexto de aplicação para criar as tabelas
        db.create_all()  # Cria as tabelas no banco de dados
    app.run(debug=True)
    