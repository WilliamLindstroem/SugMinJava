library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity timer is 
    port(
        start  : in  std_logic;
        reset  : in  std_logic;
        clk    : in  std_logic;
        seg    : out std_logic_vector(6 downto 0);
        u      : out std_logic
    );
end entity;

architecture Behavioral of timer is
    signal start_sync1  : std_logic;
    signal start_sync2  : std_logic;
    signal start_EP     : std_logic;

    signal q3, q2, q1, q0   : std_logic;

    signal data : std_logic_vector(3 downto 0);
    signal adress : std_logic_vector(3 downto 0);
    signal d_next : std_logic_vector(3 downto 0);
    
-- 7-segments avkodning, segments tänds med 0
type ROM_7seg is array (0 to 15) of std_logic_vector(6 downto 0);
constant mem : ROM_7seg := (
"1000000", -- 0
"1111001", -- 1
"0100100", -- 2
"0110000", -- 3
"0011001", -- 4
"0010010", -- 5
"0000010", -- 6
"1111000", -- 7
"0000000", -- 8
"0010000", -- 9
"0001000", -- A
"0000011", -- b
"1000110", -- C
"0100001", -- d
"0000110", -- E
"0001110" -- F
);

type ROM is array(0 to 15) of std_logic_vector(3 downto 0);
constant ROM_content : ROM := (
    0 => "0000",
    1 => "0000",
    2 => "0001",
    3 => "0010",
    4 => "0011",
    5 => "0100",
    6 => "0101",
    7 => "0110",
    8 => "0111",
    others => "0000"
);

begin

-- Enpulsare
process(clk)
begin
    if rising_edge(clk) then
        start_sync1 <= start;
        start_sync2 <= start_sync1;
    end if;
end process;
start_EP <= start_sync1 and not start_sync2;

adress <= q3 & q2 & q1 & q0;
data <= ROM_content(to_integer(unsigned(adress)));
d_next <= "1000" when (start_EP = '1' and adress = "0000") else data;

-- Tillstånds D-vippor
process(clk, reset)
begin
    if reset = '1' then
        q0 <= '0';
        q1 <= '0';
        q2 <= '0';
        q3 <= '0';
    elsif rising_edge(clk) then
        q0 <= d_next(0);
        q1 <= d_next(1);
        q2 <= d_next(2);
        q3 <= d_next(3);
    end if;
end process;

-- Lampa och 7-segment
u <= '1' when (adress = "0000") and (start_EP = '0') else '0';
seg <= mem(to_integer(unsigned(adress)));

end architecture;