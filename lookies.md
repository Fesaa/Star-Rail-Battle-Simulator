# Some stuff to look into

Small thinks that are not working correctly, that I should look into

### Power refreshing, then being removed when it's one turn
```
Feixiao started attacking FireWindImgLightningWeakEnemy0 with FOLLOW_UP
(69.19 AV) - Feixiao refreshed ValorousDamagePower (1 turn(s))
(69.19 AV) - Feixiao stacked DuranStackPower to 4
(69.19 AV) - Sword March gained 1 Charge (6 -> 7)
(69.19 AV) - Sword March advanced by 100.0% (34.005 -> 0.000)
(69.19 AV) - Feixiao hit FireWindImgLightningWeakEnemy0(238923.0) for expected crit result of 12666.525 damage
(69.19 AV) - Feixiao attacked FireWindImgLightningWeakEnemy0 (226257) for 12666.525 damage with FOLLOW_UP
(69.19 AV) - Feixiao gained 1 Stack (0 -> 1)
(69.19 AV) - Aventurine gained 1 Blind Bet (3 -> 4)
(69.19 AV) - Feixiao finished attacking FireWindImgLightningWeakEnemy0 with FOLLOW_UP
=====
(69.19 AV) - Feixiao lost ValorousDamagePower
(69.19 AV) - Feixiao lost Messenger Traversing Hackerspace 4PC 
(69.19 AV) - Feixiao delayed by speed decrease (46.396 -> 49.481)
(69.19 AV) - Feixiao decided to ULT because AlwaysUltGoal
(69.19 AV) - Feixiao used Ultimate (7.000 -> 1.000)
```
ValorousDamagePower is refreshed, but Feixiao's turn end after the FOLLOW_UP attack ends, so Powers tick down. Losing the buff, 
I think she should still have the buff here