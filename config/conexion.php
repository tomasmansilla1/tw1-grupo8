<?php
// conecta a mysql

// mas seguro con pdo
// try - catch porque puede que falle
try {
    $pdo = new PDO(
        "mysql:host=localhost ; dbname=juego_preguntas ; charset=utf8",
        "root",
        " "
    );
    // excepcion si hay error en sql
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

} 
// si falla: mensaje
catch(PDOException $e)  {
    echo "Error: " . $e->getMessage();
}