DELETE FROM pregunta;
DELETE FROM usuario;

INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

-- Inserciones para la categoría: Historia
INSERT INTO pregunta (categoria, consigna, opcionA, opcionB, opcionC, opcionD, correcta) VALUES
('HISTORIA', '¿En qué año comenzó la Primera Guerra Mundial?', '1912', '1914', '1918', '1939', 'B'),
('Historia', '¿Quién fue el primer presidente de los Estados Unidos?', 'George Washington', 'Thomas Jefferson', 'Abraham Lincoln', 'John F. Kennedy', 'A'),
('Historia', '¿Qué imperio construyó el Coliseo de Roma?', 'Imperio Griego', 'Imperio Bizantino', 'Imperio Romano', 'Imperio Persa', 'C');

-- Inserciones para la categoría: Deporte
INSERT INTO pregunta (categoria, consigna, opcionA, opcionB, opcionC, opcionD, correcta) VALUES
('DEPORTE', '¿Cada cuántos años se celebran los Juegos Olímpicos?', '2 años', '4 años', '5 años', '6 años', 'B'),
('Deporte', '¿Qué país ha ganado más Copas del Mundo de fútbol?', 'Alemania', 'Argentina', 'Italia', 'Brasil', 'D'),
('Deporte', '¿En qué deporte destaca o destacó Lewis Hamilton?', 'Tenis', 'Fórmula 1', 'Baloncesto', 'Motociclismo', 'A');

-- Inserciones para la categoría: Arte
INSERT INTO pregunta (categoria, consigna, opcionA, opcionB, opcionC, opcionD, correcta) VALUES
('ARTE', '¿Quién pintó la famosa obra "La Mona Lisa"?', 'Vincent van Gogh', 'Pablo Picasso', 'Leonardo da Vinci', 'Miguel Ángel', 'C'),
('Arte', '¿En qué país nació el movimiento artístico del Renacimiento?', 'Francia', 'Italia', 'España', 'Países Bajos', 'B'),
('Arte', '¿Quién es el autor de la escultura "El Pensador"?', 'Auguste Rodin', 'Donatello', 'Gian Lorenzo Bernini', 'Alberto Giacometti', 'A');