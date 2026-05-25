/* ============================================
   WALNUT BREWERY — Interações, Animações e API
   ============================================ */

// Tabela de Preços Oficial (Sincronizada com o Backend)
const beerPrices = {
  ipa: { name: 'Walnut IPA', price30: 450.0, price50: 680.0 },
  pilsen: { name: 'Walnut Pilsen', price30: 380.0, price50: 580.0 },
  stout: { name: 'Walnut Stout', price30: 500.0, price50: 750.0 },
  wheat: { name: 'Walnut Wheat', price30: 420.0, price50: 640.0 },
  'red-ale': { name: 'Walnut Red Ale', price30: 460.0, price50: 700.0 },
  porter: { name: 'Walnut Porter', price30: 480.0, price50: 720.0 }
};

document.addEventListener('DOMContentLoaded', () => {

  // ---------- Lucide Icons ----------
  if (typeof lucide !== 'undefined') {
    lucide.createIcons();
  }

  // ---------- Intersection Observer — Fade-in ao scroll ----------
  const fadeElements = document.querySelectorAll('.fade-in');

  const observerOptions = {
    root: null,
    rootMargin: '0px 0px -60px 0px',
    threshold: 0.15
  };

  const fadeObserver = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
        fadeObserver.unobserve(entry.target);
      }
    });
  }, observerOptions);

  fadeElements.forEach((el, index) => {
    // Atraso staggered para os cards de cerveja na grid
    if (el.classList.contains('beer-card')) {
      el.style.transitionDelay = `${(index % 3) * 0.15}s`;
    }
    fadeObserver.observe(el);
  });

  // ---------- Navbar — Mostrar após rolar fora do hero ----------
  const navbar = document.getElementById('navbar');
  const hero = document.querySelector('.hero');

  const navObserver = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (!entry.isIntersecting) {
        navbar.classList.add('show');
      } else {
        navbar.classList.remove('show');
      }
    });
  }, { threshold: 0.1 });

  if (hero) {
    navObserver.observe(hero);
  }

  // ---------- Smooth scroll para links internos ----------
  document.querySelectorAll('a[href^="#"]').forEach((link) => {
    link.addEventListener('click', (e) => {
      const href = link.getAttribute('href');
      
      // Ignora links vazios
      if (href === '#') return;

      e.preventDefault();
      const target = document.querySelector(href);
      if (target) {
        target.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    });
  });

  // ---------- Parallax sutil na logo do Hero ----------
  const heroLogo = document.querySelector('.hero-logo');

  if (heroLogo && hero) {
    window.addEventListener('scroll', () => {
      const scrollY = window.scrollY;
      const heroHeight = hero.offsetHeight || 800;

      if (scrollY < heroHeight) {
        const progress = scrollY / heroHeight;
        heroLogo.style.transform = `translateY(${scrollY * 0.25}px) scale(${1 - progress * 0.12}) rotate(${scrollY * 0.02}deg)`;
        heroLogo.style.opacity = 1 - progress * 0.8;
      }
    }, { passive: true });
  }


  // ============================================================
  // ---------- LÓGICA DO FORMULÁRIO DE RESERVAS DE CHOPP ----------
  // ============================================================

  const orderForm = document.getElementById('order-form');
  const orderContainer = document.getElementById('order-container');

  if (orderForm) {
    // Inputs
    const inputNome = document.getElementById('nome');
    const inputCpf = document.getElementById('cpf');
    const inputCelular = document.getElementById('celular');
    const selectCerveja = document.getElementById('cerveja');
    const selectTamanho = document.getElementById('tamanho');
    const inputDataRetirada = document.getElementById('dataRetirada');
    const inputDataEntrega = document.getElementById('dataEntrega');
    const inputHorario = document.getElementById('horario');
    const selectTipoFrete = document.getElementById('tipoFrete');
    const inputLocal = document.getElementById('local');
    const labelLocal = document.getElementById('local-label');

    // Elementos de Resumo
    const sumCliente = document.getElementById('summary-cliente');
    const sumCerveja = document.getElementById('summary-cerveja');
    const sumBarril = document.getElementById('summary-barril');
    const sumRetirada = document.getElementById('summary-retirada');
    const sumDevolucao = document.getElementById('summary-devolucao');
    const sumSubtotal = document.getElementById('summary-subtotal');
    const sumFrete = document.getElementById('summary-frete');
    const sumTotal = document.getElementById('summary-total');

    // Formatação em tempo real para celular e CPF (Máscaras Simples)
    inputCpf.addEventListener('input', (e) => {
      let v = e.target.value.replace(/\D/g, "");
      if (v.length > 11) v = v.slice(0, 11);
      if (v.length > 9) {
        v = v.replace(/^(\d{3})(\d{3})(\d{3})(\d{1,2})$/, "$1.$2.$3-$4");
      } else if (v.length > 6) {
        v = v.replace(/^(\d{3})(\d{3})(\d{1,3})$/, "$1.$2.$3");
      } else if (v.length > 3) {
        v = v.replace(/^(\d{3})(\d{1,3})$/, "$1.$2");
      }
      e.target.value = v;
    });

    inputCelular.addEventListener('input', (e) => {
      let v = e.target.value.replace(/\D/g, "");
      if (v.length > 11) v = v.slice(0, 11);
      if (v.length > 10) {
        v = v.replace(/^(\d{2})(\d{5})(\d{4})$/, "($1) $2-$3");
      } else if (v.length > 5) {
        v = v.replace(/^(\d{2})(\d{4})(\d{0,4})$/, "($1) $2-$3");
      } else if (v.length > 2) {
        v = v.replace(/^(\d{2})(\d{0,5})$/, "($1) $2");
      } else if (v.length > 0) {
        v = v.replace(/^(\d{0,2})$/, "($1");
      }
      e.target.value = v;
    });

    // Função de atualização do painel de resumo
    const atualizarResumo = () => {
      // 1. Cliente
      const nome = inputNome.value.trim();
      sumCliente.textContent = nome ? nome.split(' ')[0] : '—';

      // 2. Cerveja e Preços
      const cervejaId = selectCerveja.value;
      const tamanho = selectTamanho.value;
      
      let valorBarril = 0;
      if (cervejaId && beerPrices[cervejaId]) {
        const cerveja = beerPrices[cervejaId];
        sumCerveja.textContent = cerveja.name;
        valorBarril = (tamanho === '50') ? cerveja.price50 : cerveja.price30;
        sumSubtotal.textContent = `R$ ${valorBarril.toFixed(2).replace('.', ',')}`;
      } else {
        sumCerveja.textContent = '—';
        sumSubtotal.textContent = '—';
      }

      sumBarril.textContent = `${tamanho} Litros`;

      // 3. Datas
      const formatarData = (dataStr) => {
        if (!dataStr) return '—';
        const partes = dataStr.split('-');
        return `${partes[2]}/${partes[1]}/${partes[0]}`; // Formato dd/mm/aaaa
      };
      sumRetirada.textContent = formatarData(inputDataRetirada.value);
      sumDevolucao.textContent = formatarData(inputDataEntrega.value);

      // 4. Frete
      const tipoFrete = selectTipoFrete.value;
      let valorFrete = 0;
      if (tipoFrete === 'entrega') {
        valorFrete = 80.00;
        sumFrete.textContent = 'R$ 80,00';
        labelLocal.innerHTML = 'Endereço de Entrega <span style="color:red">*</span>';
        inputLocal.placeholder = 'Rua, nº, bairro, cidade';
        inputLocal.required = true;
      } else {
        valorFrete = 0;
        sumFrete.textContent = 'Grátis';
        labelLocal.textContent = 'Observações (opcional)';
        inputLocal.placeholder = 'Ex: Buscar na porta dos fundos...';
        inputLocal.required = false;
      }

      // 5. Total
      if (valorBarril > 0) {
        const total = valorBarril + valorFrete;
        sumTotal.textContent = `R$ ${total.toFixed(2).replace('.', ',')}`;
      } else {
        sumTotal.textContent = '—';
      }
    };

    // Escutar eventos de alteração para atualizar o resumo
    [inputNome, selectCerveja, selectTamanho, inputDataRetirada, inputDataEntrega, selectTipoFrete].forEach(el => {
      el.addEventListener('input', atualizarResumo);
      el.addEventListener('change', atualizarResumo);
    });

    // Submissão do Formulário integrada ao Backend Spring Boot
    orderForm.addEventListener('submit', (e) => {
      e.preventDefault();

      const cervejaId = selectCerveja.value;
      if (!cervejaId) {
        alert('Por favor, selecione uma cerveja.');
        return;
      }

      const btnSubmit = document.getElementById('btn-submit-order');
      const textoOriginal = btnSubmit.textContent;
      btnSubmit.textContent = 'Enviando Reserva...';
      btnSubmit.disabled = true;

      // Monta o objeto JSON mapeado para a classe Java Pedido
      const payload = {
        nome: inputNome.value.trim(),
        cpf: inputCpf.value.replace(/\D/g, ""), // Salva apenas números
        celular: inputCelular.value,
        cerveja: cervejaId,
        tamanho: parseInt(selectTamanho.value),
        dataRetirada: inputDataRetirada.value,
        dataEntrega: inputDataEntrega.value,
        horario: inputHorario.value,
        tipoFrete: selectTipoFrete.value,
        local: inputLocal.value.trim()
      };

      // Chamada HTTP assíncrona (Fetch API) para o endpoint Java
      fetch('/api/pedidos', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      })
      .then(response => {
        if (!response.ok) {
          return response.json().then(err => { throw new Error(err.mensagem || 'Erro na requisição'); });
        }
        return response.json();
      })
      .then(pedidoSalvo => {
        // Exibir tela de sucesso idêntica ao design do Next.js
        const beerName = beerPrices[pedidoSalvo.cerveja]?.name || 'Chopp';
        
        orderContainer.innerHTML = `
          <div class="order-success glass fade-in visible">
            <div class="order-success-icon">🍺</div>
            <h3>Reserva Solicitada com Sucesso!</h3>
            <p>
              Obrigado, <strong>${pedidoSalvo.nome}</strong>! Recebemos seu pedido de reserva do barril de 
              <strong>${pedidoSalvo.tamanho}L</strong> de <strong>${beerName}</strong> (Valor Total: R$ ${pedidoSalvo.valorTotal.toFixed(2).replace('.', ',')}).
              <br><br>
              A nossa equipe entrará em contato pelo número <strong>${pedidoSalvo.celular}</strong> em breve para os detalhes finais.
            </p>
            <button class="order-btn" id="btn-novo-pedido" style="width: auto; padding: 0.9em 2.5rem; margin-top: 0;">
              Fazer Novo Pedido
            </button>
          </div>
        `;

        // Rola até a seção para ver o sucesso perfeitamente
        document.getElementById('pedidos').scrollIntoView({ behavior: 'smooth', block: 'start' });

        // Adiciona evento ao botão de reiniciar
        document.getElementById('btn-novo-pedido').addEventListener('click', () => {
          location.reload(); // Recarrega a página de forma simples e limpa para resetar
        });
      })
      .catch(error => {
        console.error('Erro ao enviar pedido:', error);
        alert('Erro ao enviar reserva: ' + error.message + '\nPor favor, tente novamente.');
        btnSubmit.textContent = textoOriginal;
        btnSubmit.disabled = false;
      });

    });

    // Executar uma vez no load para inicializar o resumo
    atualizarResumo();
  }

});
