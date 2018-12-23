program Nome ;
const
c1 = 1 ; {ok}
c2 = 2 ;
var
x : integer ; {ok}
y : double ;
z : integer ;
i : integer ;
texto : string ;
begin
    x := 10 ; {ok}
    y := 10.5 ;
    z := 10 ;
    i := 10 ;
    texto := 'um texto qualquer' ; {j := 0 ;}
    if x > 0 then
    begin
        write( 'x > 0' ) ;
    end ;
    for i := 0 to 10 do
    begin
        write( i ) ;
    end ;
    while z < 21 do
    begin
    z := z + 1 ;
    write( z ) ;
    end ;
    y := 10.5 * 20.0 ;
    x := x * 10 ;
    {x := x * 1.7 ;}
    {i := 2 / 0 ;}
    texto := 'uma ' + ' string' ;
    read( x ) ; {parada}
end . 