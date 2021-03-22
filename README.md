# RoboCode

## Phase 1
A primeira fase deste projeto envolve a conceção e implementação  de  um  odómetro, para medir  a  distância  percorrida por um robô em cada ronda de uma batalha, bem como a implementação de técnicas de planeamento de trajetórias para circum-navegação de 3 obstáculos. 
### Odómetro
O cálculo da distância é feita através da Distância Euclidiana. Sabendo a posição anterior e atual do robô é possível ir atualizando este valor. 
### Circum Navegação
A solução proposta para esta etapa consiste em movimentar o robô numa espécie de quadrilátero (semelhante ao que está a ser usado no cálculo do perímetro pelo Standard Odometer) que abrange os três obstáculos no mapa. Para tal, é imperativo que o robô contorne os objetos mas que o faça de modo a percorrer o menor caminho possível. 

## Phase 2
Pretende-se conceber equipas de 5 elementos que demonstrem Comportamentos Sociais e de grupo e que permitam discutir e aplicar os conhecimentos adquiridos na UC de Agentes Inteligentes, nomeadamente a cooperação entre agentes num Sistema Multi-Agente, com o objetivo de resolver um problema comum.

### NSYNC
Esta equipa é constituída por um robô líder e quatro droids, da classe JustinTimbs que coopera para efetuar uma dança sincronizada, não se preocupando com os adversários. Quando esta equipa é colocada em campo, o líder começa por se deslocar para o centro do mapa e mandar uma mensagem a cada membro da equipa, indicando a posição para a qual se devem deslocar. Quando os JustinTimbs já se encontram no local indicado e após o líder ter recebido a confirmação que todos estão no lugar, o líder dá ordem para todos iniciarem a dança da vitória do Robocode.

### SA Team
Esta equipa é constituída por um robô da classe SA (que funciona como líder), quatro robôs da classe buddies e tem uma utilidade meramente lúdica, uma vez que não permite o ataque. Nesta equipa, os quatro robôs buddies seguem o seu líder que os leva a "escrever" as letras SA, iniciais da UC que solicitou a realização deste trabalho, percorrendo para isso o caminho demarcado a vermelho.

Através da construção deste Team, foi possível treinar a comunicação entre os vários Agentes (robôs)  e a coordenação entre todos eles na procura da rota que lhes permite seguir o líder e evitar o choque.

### Tracker Team
Esta equipa é constituída por um robô líder TrackerLeader e quatro robôs TrackerSquad cujo objetivo é proteger o líder colocando o resto da equipa numa formação em semicírculo.


## Phase 3


## Authors

-   **Carolina Marques:** [CarolinaRMarques](https://github.com/CarolinaRMarques)
-   **Constança Elias:** [ConstancaElias](https://github.com/ConstancaElias)
-   **Maria Barbosa:** [maria85290](https://github.com/maria85290)
-   **Renata Ribeiro:** [renataR30](https://github.com/renataR30)
