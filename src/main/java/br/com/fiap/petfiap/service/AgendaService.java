package br.com.fiap.petfiap.service;

import br.com.fiap.petfiap.exception.AtendimentoNaoEncontradoException;
import br.com.fiap.petfiap.exception.HorarioOcupadoException;
import br.com.fiap.petfiap.exception.StatusInvalidoException;
import br.com.fiap.petfiap.model.Atendimento;
import br.com.fiap.petfiap.repository.AtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// Regras de agenda do PetFiap: agendar, concluir e cancelar atendimentos.
@Service
public class AgendaService {

    @Autowired
    private AtendimentoRepository repository;

    // Agenda um novo atendimento: recusa horario ja ocupado pelo mesmo pet.
    public Atendimento agendar(Atendimento novo) {

        if (novo.getDataHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data e hora do agendamento nao podem estar no passado");
        }
        // Busca atendimentos existentes do pet para verificar conflito de horario
        List<Atendimento> atendimentosExistentes = repository.findByPetNome(novo.getPetNome());

        for (Atendimento existente : atendimentosExistentes) {

            if ("AGENDADO".equals(existente.getStatus()) && existente.getDataHora().isEqual(novo.getDataHora())) {
                throw new HorarioOcupadoException("O pet ja possui um atendimento agendado para este horario: " + novo.getDataHora());
            }
        }
        Atendimento salvo = repository.save(novo);
        System.out.println("Recibo: atendimento " + salvo.getProtocolo()
                + " agendado para " + salvo.getPetNome() + " (tutor " + salvo.getTutorNome() + ")");
        return salvo;
    }

    // Busca pelo id; nunca retorna null, o orElseThrow garante a excecao.
    public Atendimento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AtendimentoNaoEncontradoException("Atendimento nao encontrado: " + id));
    }

    // Conclui o atendimento (status AGENDADO -> CONCLUIDO).
    public Atendimento concluir(Long id) {
        Atendimento atendimento = buscarPorId(id);
        if (!"AGENDADO".equalsIgnoreCase(atendimento.getStatus())) {
            throw new StatusInvalidoException("Nao e possivel concluir um atendimento com status: " + atendimento.getStatus());
        }

        atendimento.setStatus("CONCLUIDO");
        return repository.save(atendimento);
    }

    public Atendimento cancelar(Long id) {
        Atendimento atendimento = buscarPorId(id);
        if ("CONCLUIDO".equalsIgnoreCase(atendimento.getStatus()) || "CANCELADO".equalsIgnoreCase(atendimento.getStatus())) {
            throw new StatusInvalidoException("Nao e possivel cancelar um atendimento com status: " + atendimento.getStatus());
        }
        atendimento.setStatus("CANCELADO");
        return repository.save(atendimento);
    }

    // Lista os atendimentos de um pet.
    public List<Atendimento> buscarPorPet(String petNome) {
        return repository.findByPetNome(petNome);
    }
}
