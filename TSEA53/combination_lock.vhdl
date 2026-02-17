library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity combination_lock is
    port(
        x1, x0 : in std_logic;
        clk : in std_logic;
        clr : in std_logic;
        u : out std_logic
        );
end entity;

architecture Behavioral of combination_lock is
    signal x1_sync : std_logic;
    signal x0_sync : std_logic;

    signal q1, q0 : std_logic;
    signal d1, d0 : std_logic;

    type ROM is array(0 to 15) of std_logic_vector(3 downto 0);
    constant ROM_content : ROM := (0 => "0001",
                                   1 => "0000",
                                   2 => "0000",
                                   3 => "0000",
                                   4 => "0001",
                                   5 => "0010",
                                   6 => "0000",
                                   7 => "0000",
                                   8 => "0000",
                                   9 => "0010",
                                   10 => "0000",
                                   11 => "0011",
                                   12 => "0100",
                                   13 => "0111",
                                   14 => "0111",
                                   15 => "0111");
    signal data     : std_logic_vector(3 downto 0);
    signal adress   : std_logic_vector(3 downto 0);

begin

-- Synkroniserings vippor
    process(clk, clr)
    begin
        if clr = '1' then
            x1_sync <= '0';
            x0_sync <= '0';
        elsif rising_edge(clk) then
            x1_sync <= x1;
            x0_sync <= x0;
        end if;
    end process;

    adress <= q1 & q0 & x1_sync & x0_sync;
    data <= ROM_content(to_integer(unsigned(adress)));

    d0 <= data(0);
    d1 <= data(1);
    u <= data(2);

-- Tillstånds vippor
    process (clk, clr)
    begin
        if clr = '1' then
            q1 <= '0';
            q0 <= '0';
        elsif rising_edge(clk) then
            q1 <= d1;
            q0 <= d0;
        end if;
    end process;

end architecture;
