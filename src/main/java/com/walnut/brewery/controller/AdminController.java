package com.walnut.brewery.controller;

import com.walnut.brewery.model.Pedido;
import com.walnut.brewery.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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

        for (Pedido p : pedidos) {
            totalFaturamento += p.getValorTotal();
            if (p.getTamanho() != null) {
                if (p.getTamanho() == 30) {
                    barris30++;
                } else if (p.getTamanho() == 50) {
                    barris50++;
                }
            }
        }

        // manda as variaveis pro thymeleaf

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("totalPedidos", pedidos.size());
        model.addAttribute("totalFaturamento", totalFaturamento);
        model.addAttribute("barris30", barris30);
        model.addAttribute("barris50", barris50);

        // chama o template admin.html

        return "admin";
    }
}
