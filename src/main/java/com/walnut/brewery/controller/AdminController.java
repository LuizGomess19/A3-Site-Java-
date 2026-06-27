package com.walnut.brewery.controller;

import com.walnut.brewery.model.Pedido;
import com.walnut.brewery.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Rota da pagina do painel do prof

    @GetMapping("/admin")
    public String exibirPainelAdmin(Model model) {
        List<Pedido> pedidos = pedidoRepository.findAll();

        // System.out.println("Acessou o admin");

        // faz as contas pra exibir la no html

        double totalFaturamento = 0.0;
        int barris30 = 0;
        int barris50 = 0;
        int litrosVendidos = 0;
        int entregas = 0;
        int retiradas = 0;

Map<String, Integer> vendasPorCerveja = new HashMap<String, Integer>();


        for (Pedido p : pedidos) {
            totalFaturamento += p.getValorTotal();
            if (p.getTamanho() != null) {
                if (p.getTamanho() == 30) {
                    barris30++;
                    if (p.getTamanho() != null) {
    litrosVendidos += p.getTamanho();
}

if ("entrega".equalsIgnoreCase(p.getTipoFrete())) {
    entregas++;
} else {
    retiradas++;
}

String cerveja = p.getCerveja();

if (cerveja != null) {
    cerveja = cerveja.toLowerCase();
    vendasPorCerveja.put(
            cerveja,
            vendasPorCerveja.getOrDefault(cerveja, 0) + 1
    );
                } else if (p.getTamanho() == 50) {
                    barris50++;
                }
            }
        }
String cervejaMaisVendida = "Nenhuma";
int maiorQuantidade = 0;

for (Map.Entry<String, Integer> entry : vendasPorCerveja.entrySet()) {

    if (entry.getValue() > maiorQuantidade) {
        maiorQuantidade = entry.getValue();
        cervejaMaisVendida = entry.getKey();
    }
}
        // manda as variaveis pro thymeleaf

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("totalPedidos", pedidos.size());
        model.addAttribute("totalFaturamento", totalFaturamento);
        model.addAttribute("barris30", barris30);
        model.addAttribute("barris50", barris50);

        // chama o template admin.html
        model.addAttribute("litrosVendidos", litrosVendidos);
        model.addAttribute("entregas", entregas);
        model.addAttribute("retiradas", retiradas);
        model.addAttribute("cervejaMaisVendida", cervejaMaisVendida);

        return "admin";
    }
}
