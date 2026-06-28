INSERT INTO usuarios(id, email, username, password, rol, activo)
SELECT null, 'test@unlam.edu.ar', 'admin', 'test', 'ADMIN', true
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE email = 'test@unlam.edu.ar'
);

INSERT IGNORE INTO usuarios(id, email, username, password, rol, puntaje, partidasGanadasSeguidas)VALUES
(2, 'mauricio@unlam.edu.ar', 'mauricio', '1234', 'JUGADOR', 120, 15),
(3, 'juan@unlam.edu.ar', 'juan', '1234', 'JUGADOR', 95, 8),
(4, 'ana@unlam.edu.ar', 'ana', '1234', 'JUGADOR', 150, 22),
(5, 'pedro@unlam.edu.ar', 'pedro', '1234', 'JUGADOR', 75, 4),
(6, 'lucia@unlam.edu.ar', 'lucia', '1234', 'JUGADOR', 180, 30),
(7, 'martin@unlam.edu.ar', 'martin', '1234', 'JUGADOR', 110, 12),
(8, 'camila@unlam.edu.ar', 'camila', '1234', 'JUGADOR', 135, 18),
(9, 'sofia@unlam.edu.ar', 'sofia', '1234', 'JUGADOR', 60, 2),
(10, 'nicolas@unlam.edu.ar', 'nicolas', '1234', 'JUGADOR', 200, 35),
(11, 'florencia@unlam.edu.ar', 'florencia', '1234', 'JUGADOR', 145, 20);

INSERT INTO partida(categoria, fecha, puntajeObtenido, inicio_partida, final_partida, esVictoria, usuarios_id, respuesta_id)VALUES
('Historia', '2026-08-01 10:00:00', 10, '10:00:00', '10:01:30', true,  2, null),
('Deporte', '2026-08-01 11:00:00',  8, '11:00:00', '11:02:00', true,  3, null),
('Arte', '2026-08-01 12:00:00',  6, '12:00:00', '12:04:00', true, 4, null),
('Geografia', '2026-08-01 13:00:00', 10, '13:00:00', '13:01:10', true,  5, null),
('Historia','2026-08-01 14:00:00',  5, '14:00:00', '14:05:00', true, 6, null),
('Historia', '2026-08-01 15:00:00', 10, '15:00:00', '15:01:00', true,  7, null),
('Deporte', '2026-08-01 16:00:00',  7, '16:00:00', '16:03:00', true,  8, null),
('Deporte', '2026-08-01 17:00:00', 10, '17:00:00', '17:00:50', true,  9, null),
('Deporte', '2026-08-01 18:00:00',  9, '18:00:00', '18:02:20', true,  10, null),
('Arte', '2026-08-01 19:00:00', 10, '19:00:00', '19:01:40', true, 11, null),
('Geografia', '2026-08-01 19:00:00', 10, '19:00:00', '19:01:40', false, 11, null);