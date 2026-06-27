import React, { useEffect, useState } from 'react';
import { LogOut, Wallet, ArrowRightLeft, UserCircle } from 'lucide-react';

const Dashboard = ({ onLogout }) => {
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulando um fetch dos dados do usuário
    const fetchUserData = async () => {
      try {
        const token = localStorage.getItem('vaultbank_token');
        // A API de /me ou dados do usuário ainda precisa ser construída,
        // mas vamos simular por enquanto ou chamar a API de contas se existir
        
        // Simulação
        setTimeout(() => {
          setUserData({
            name: "Luis Gustavo",
            balance: 15000.50,
            accountNumber: "12345-6"
          });
          setLoading(false);
        }, 1000);

      } catch (error) {
        console.error("Erro ao carregar dados", error);
      }
    };

    fetchUserData();
  }, []);

  if (loading) {
    return <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>Carregando...</div>;
  }

  return (
    <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem', width: '100%' }}>
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '3rem' }}>
        <div>
          <h1 className="text-gradient" style={{ margin: 0, fontSize: '2rem' }}>VaultBank</h1>
          <p style={{ color: 'var(--text-muted)' }}>Bem-vindo de volta, {userData?.name}</p>
        </div>
        <button onClick={onLogout} className="btn" style={{ background: 'transparent', color: 'var(--text-muted)', border: '1px solid var(--border-color)' }}>
          <LogOut size={18} style={{ marginRight: '0.5rem' }} /> Sair
        </button>
      </header>

      <main style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '2rem' }}>
        {/* Saldo */}
        <div className="glass-card">
          <div style={{ display: 'flex', alignItems: 'center', marginBottom: '1rem', color: 'var(--text-muted)' }}>
            <Wallet size={20} style={{ marginRight: '0.5rem' }} />
            <span>Saldo Disponível</span>
          </div>
          <h2 style={{ fontSize: '2.5rem', margin: 0 }}>
            {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(userData?.balance || 0)}
          </h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.875rem', marginTop: '1rem' }}>
            Conta Corrente: {userData?.accountNumber}
          </p>
        </div>

        {/* Ações */}
        <div className="glass-card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', gap: '1rem' }}>
          <button className="btn btn-primary" style={{ width: '100%' }}>
            <ArrowRightLeft size={18} style={{ marginRight: '0.5rem' }} /> Realizar Transferência
          </button>
          <button className="btn" style={{ width: '100%', background: 'rgba(255,255,255,0.05)', color: 'white' }}>
            <UserCircle size={18} style={{ marginRight: '0.5rem' }} /> Meu Perfil
          </button>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;
