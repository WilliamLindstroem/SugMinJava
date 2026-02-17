library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity alarm is 
    port(
        start  : in std_logic;
        reset  : in std_logic;
        clk    : in std_logic;
        seg    : out std_logic;
        u      : out std_logic
        );
end entity;

architecture Behavioral of alarm is
    signal start_sync1  : std_logic;
    signal start_sync2  : std_logic;

    signal d2, d1, d0   : std_logic;
    signal q2, q1, q0   : std_logic;

    signal start_EP     : std_logic;

    type ROM is array(0 to 15) of std_logic_vector(3 downto 0);
    constant ROM_content : ROM := (0 => "0000",
                                   1 => "0000",
                                   2 => "0001",
                                   3 => "0010",
                                   4 => "0011",
                                   5 => "0100",
                                   6 => "0101",
                                   7 => "0110",
                                   8 => "0111",
                                   9 => "1111",
                                   10 => "1111",
                                   11 => "1111",
                                   12 => "1111",
                                   13 => "1111",
                                   14 => "1111",
                                   15 => "1111");
    signal data     : std_logic_vector(3 downto 0);
    signal adress   : std_logic_vector(3 downto 0);

begin

-- Enpulsare
process(clk)
begin
    if rising_edge(clk) then 
        start_sync1 <= start;
        start_sync2 <= start_sync1;
    end if;
end process;

start_EP <= start_sync1 and (not start_sync2);

-- Prom
adress <= start_EP & q0 & q1 & q2;
data <= ROM_content(to_integer(unsigned(adress)));

d0 <= data(0);
d1 <= data(1);
d2 <= data(2);

-- Tillstånds-vippor
process(clk, reset)
begin
    if reset = '1' then
        q0 <= '0';
        q1 <= '0';
        q2 <= '0';
    elsif rising_edge(clk) then
        q0 <= d0;
        q1 <= d1;
        q2 <= d2;
    end if;
end process;

-- Sjusegmentsdisplay och alarm lampa

u <= q0 nor q1 nor q2